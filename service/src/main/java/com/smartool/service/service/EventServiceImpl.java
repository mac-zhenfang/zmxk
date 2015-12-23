package com.smartool.service.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.google.common.base.Strings;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.EnrollAttendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.EventTask;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Round;
import com.smartool.common.dto.Team;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.Constants;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserSessionManager;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.RoundDao;
import com.smartool.service.dao.TeamDao;

public class EventServiceImpl implements EventService {

	private static Logger logger = Logger.getLogger(EventServiceImpl.class);
	@Autowired
	private EventDao eventDao;

	@Autowired
	AttendeeDao attendeeDao;

	@Autowired
	private KidDao kidDao;

	@Autowired
	private TeamDao teamDao;

	@Autowired
	private SmartoolServiceConfig config;

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private FileService fileService;

	private ReentrantLock enrollLock = new ReentrantLock(); // FIXME single
															// service
	private ReentrantLock batchUploadLock = new ReentrantLock(); // FIXME single
																	// service

	private static Random r = new Random();

	@Autowired
	private RoundDao roundDao;

	private final static String EVENT_COMPLETE_TIMER_KEY = "EventCompleteExecutor";

	public void iocInit() throws SchedulerException {

		if (scheduler.getTrigger(TriggerKey.triggerKey("EventStartNotificationJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventStartNotificationJob.class)
					.withIdentity(JobKey.jobKey("EventStartNotificationJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(TriggerKey.triggerKey("EventStartNotificationJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}

		if (scheduler.getTrigger(TriggerKey.triggerKey("EventBackupJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventBackupJob.class)
					.withIdentity(JobKey.jobKey("EventBackupJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("EventBackupJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}

		if (scheduler.getTrigger(TriggerKey.triggerKey(EVENT_COMPLETE_TIMER_KEY)) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventBackupJob.class)
					.withIdentity(JobKey.jobKey(EVENT_COMPLETE_TIMER_KEY)).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(EVENT_COMPLETE_TIMER_KEY))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}

	}

	@Override
	public Attendee complete(Attendee attendee, String eventId) {

		// TagId to seqs
		Map<String, Set<Integer>> groupMap = new HashMap<String, Set<Integer>>();
		List<Attendee> existeAttendees = attendeeDao.getAttendeeFromEvent(eventId);
		// AttendeeId to Attendee
		Map<String, Attendee> existeAttendeeMap = new HashMap<String, Attendee>();
		for (Attendee existeAttendee : existeAttendees) {
			if (existeAttendee.getKidId() != null) {
				existeAttendeeMap.put(existeAttendee.getId(), existeAttendee);
				// Update
				String tagId = existeAttendee.getTagId();
				if (tagId == null) {
					tagId = Constants.NULL_TAG_ID;
				}
				if (!groupMap.containsKey(tagId)) {
					groupMap.put(tagId, new TreeSet<Integer>());
				}
				if (existeAttendee.getSeq() != null) {
					groupMap.get(tagId).add(existeAttendee.getSeq());
				}
			}
		}
		// clean rank

		if (attendee.getScore() == 0) {
			attendee.setStatus(1);
		} else {
			attendee.setStatus(2);
		}

		// clean attendees
		if (attendee.getStatus() == 1) {
			return attendeeDao.complete(attendee);
		}

		Map<String, Attendee> attendeesPerRound = attendeeDao.getAttendeesFromEvent(eventId, attendee.getRoundId());
		attendeesPerRound.put(attendee.getId(), attendee);
		List<Attendee> listAttendees = new ArrayList<>();
		for (Entry<String, Attendee> entry : attendeesPerRound.entrySet()) {
			listAttendees.add(entry.getValue());
		}
		Collections.sort(listAttendees, new Comparator<Attendee>() {
			@Override
			public int compare(Attendee o1, Attendee o2) {
				int ret = o1.getScore() <= o2.getScore() ? -1 : 1;
				return ret;
			}
		});
		for (int i = 0; i < listAttendees.size(); i++) {
			Attendee toSave = listAttendees.get(i);
			toSave.setRank(i + 1);
			Attendee ret = attendeeDao.complete(toSave);
			if (ret.getId().equals(attendee.getId())) {
				attendee = ret;
			}
		}
		return attendee;

	}

	@Override
	public Attendee enroll(String eventId, EnrollAttendee enrollAttendee, String teamId) {

		User user = UserSessionManager.getSessionUser();
		List<Attendee> attendees = attendeeDao.getAllPendingAttendees(eventId);

		Event event = eventDao.getEvent(eventId);
		Kid returnKid = null;
		String givenKidId = enrollAttendee.getKidId();
		if (enrollAttendee.getKid() != null && Strings.isNullOrEmpty(enrollAttendee.getKidId())) {
			Kid kid = enrollAttendee.getKid();
			if (Strings.isNullOrEmpty(kid.getUserId())) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.MISS_USER_ID);
			}
			kid.setId(CommonUtils.getRandomUUID());
			returnKid = kidDao.create(kid);
			givenKidId = returnKid.getId();
			enrollAttendee.setUserId(returnKid.getUserId());
		} else {
			returnKid = kidDao.get(enrollAttendee.getKidId());
		}
		if (teamId != null) {
			Team team = teamDao.memberOf(returnKid.getId(), teamId);
			if (null == team && teamId != null) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_BELONG_TO_TEAM);
			} else if (null != team && !user.getRoleId().equals("2") && !user.getId().equals(team.getOwnerId())) { // only
																													// admin
																													// or
																													// owner
																													// can
																													// enroll
																													// team
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
			}

			List<String> members = teamDao.getMembers(teamId);

			if (members.size() < team.getMinSize()) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_REACH_MIN_TEAM_SIZE);
			}
		}

		if (returnKid.getFirstTimeAttendEvent() == 0) {
			kidDao.setFirstAttendEventTime(returnKid.getId(), event.getEventTime());
		}

		int quota = event.getQuota();
		List<Attendee> enrolledAttendees = attendeeDao.getAttendeeFromEvent(eventId);

		if (exceedQuota(quota, enrolledAttendees)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_QUOTA_ENROLL_MESSAGE);
		}
		// FIXME return Kid ID
		if (isDupliatedEnrolled(enrolledAttendees, givenKidId)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_ENROLL_MESSAGE);
		}
		int rt = quota >= attendees.size() ? attendees.size() : quota;
		return internalEnroll(enrollAttendee, attendees, returnKid, eventId, rt, rt);

	}

	private Attendee internalEnroll(EnrollAttendee enrollAttendee, List<Attendee> attendees, Kid returnKid,
			String eventId, int retryTimes, int quota) {
		try {
			enrollLock.tryLock(5000, TimeUnit.MILLISECONDS);
			Map<String, Round> roundMap = groupRoundsByLevel();
			Round round = selectRound(1, eventId, config.getDefaultRoundName(), roundMap);
			if (null == round) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_ROUND_MAX);
			}

			for (int i = 0; i < retryTimes; i++) {
				int num = r.nextInt(quota);
				Attendee attendee = attendees.get(num);
				attendee.setStatus(1); // change to enroll status
				// FIXME: check Kid/User/Event if valid
				attendee.setEventId(eventId);
				attendee.setUserId(enrollAttendee.getUserId());
				attendee.setRoundId(round.getId());
				String kidId = enrollAttendee.getKidId();

				if (Strings.isNullOrEmpty(enrollAttendee.getKidId()) && returnKid != null) {
					kidId = returnKid.getId();
				}
				if (null == kidId) {
					throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_KID_SELECTION);
				}
				attendee.setKidId(kidId);
				Attendee createdAttendee = attendeeDao.enroll(attendee);

				Kid kid = kidDao.get(kidId);
				createdAttendee.setKidName(kid.getName());
				if (null != createdAttendee) {
					logger.info(" enroll kid " + kidId);
					return createdAttendee;
				}
			}
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessages.WRONG_KID_SELECTION);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessages.WRONG_KID_SELECTION);
		} finally {
			enrollLock.unlock();
		}
	}

	private boolean exceedQuota(int quota, List<Attendee> enrolledAttendees) {
		return quota == enrolledAttendees.size();
	}

	private boolean isDupliatedEnrolled(List<Attendee> attendees, String inputKidId) {

		for (Attendee attendee : attendees) {
			String kidId = attendee.getKidId();
			if (!Strings.isNullOrEmpty(kidId) && kidId.equals(inputKidId)) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Round> groupRoundsByLevel() {
		List<Round> rounds = roundDao.listAll(1);
		Map<String, Round> map = new HashMap<String, Round>();
		for (Round round : rounds) {
			map.put(round.getShortName(), round);
		}
		return map;
	}

	private Round selectRound(int level, String eventId, String shortName, Map<String, Round> roundMap) {
		List<Attendee> attendees = attendeeDao.listRoundsByLevelName(level, shortName, eventId);

		int attendeeSize = attendees == null ? 0 : attendees.size();
		Round round = roundMap.get(shortName);

		if (round == null) {
			return null;
		}
		if (attendeeSize < config.getRoundAttendNum()) {
			return round;
		} else {
			String nextShortName = nextShortName(shortName);
			return selectRound(level, eventId, nextShortName, roundMap);
		}
	}

	private String nextShortName(String currentShortName) {
		String c;
		if (currentShortName.startsWith("0")) {
			c = "0" + (Integer.parseInt(currentShortName.substring(1)) + 1);
		} else {
			c = Integer.parseInt(currentShortName) + "";
		}
		return c;
	}

	@Override
	public void batchComplete(String eventId, byte[] binaryFile) {
		// Save to Aliyun & update JobDetail
		InputStream is = new ByteArrayInputStream(binaryFile);

		String url = fileService.uploadExcel(eventId, is);

		JobKey jobKey = JobKey.jobKey(EVENT_COMPLETE_TIMER_KEY);
		try {
			if (!scheduler.checkExists(jobKey)) {
				throw new SmartoolException(HttpStatus.NOT_IMPLEMENTED.value(), ErrorMessages.JOB_NOT_INITIALIZED);
			}

			batchUploadLock.tryLock(5, TimeUnit.SECONDS);

			JobDetail jobDetail = scheduler.getJobDetail(jobKey);

			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			
			EventTask task = new EventTask();
			task.setDataUrl(url);
			task.setEventId(eventId);
			task.setStatus(0);
			task.setCreatedTime(System.currentTimeMillis());
		
			jobDataMap.put(eventId, task);

			jobDetail = JobBuilder.newJob(BatchAttendeeCompleteJob.class).withIdentity(jobKey).setJobData(jobDataMap)
					.build();
			scheduler.addJob(jobDetail, true);
			
		} catch (Exception e) {
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ErrorMessages.SERVER_ERROR_CONTACT_ADMIN_JOB_REGISTER_ERROR);

		} finally {
			batchUploadLock.unlock();
		}

	}

}
