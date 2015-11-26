package com.smartool.service.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.EnrollAttendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Round;
import com.smartool.common.dto.Team;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.RoundDao;
import com.smartool.service.dao.TagDao;
import com.smartool.service.dao.TeamDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventController extends BaseController {
	private final static String NULL_TAG_ID = "NULL_TAG_ID";

	private static Logger logger = Logger.getLogger(EventController.class);
	@Autowired
	private EventDao eventDao;

	@Autowired
	private AttendeeDao attendeeDao;

	@Autowired
	private KidDao kidDao;

	@Autowired
	private TeamDao teamDao;

	// @Autowired
	// private UserDao userDao;

	@Autowired
	private RoundDao roundDao;

	@Autowired
	private TagDao tagDao;

	private int attendeePerGourpLimitation = 100;

	private static Random r = new Random();

	@Autowired
	private SmartoolServiceConfig config;

	private ReentrantLock enrollLock = new ReentrantLock();

	/**
	 * List events by site
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/{siteId}/events", method = RequestMethod.GET)
	public List<Event> getEventsBySite(@PathVariable String siteId) {
		return eventDao.listAllEvent(siteId);
	}

	/**
	 * List
	 * 
	 * @RequestMapping(value = "/events")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public List<Event> getEvents(@RequestParam(value = "status", required = false) String status) {
		List<Event> returnList = new ArrayList<Event>();
		if (Strings.isNullOrEmpty(status)) {
			returnList = eventDao.listAllEvent();
		} else {
			int s;
			try {
				s = Integer.parseInt(status);
			} catch (Exception e) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_EVENT_STATUS);
			}
			returnList = eventDao.listAllEvent(s);
		}
		return returnList;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/events/recent", method = RequestMethod.GET)
	public List<Event> getRecentEvents(@RequestParam(value = "start", required = true) long start,
			@RequestParam(value = "end", required = true) long end) {
		return eventDao.ListAllEvent(0, start, end);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events/search", method = RequestMethod.GET)
	public List<Event> search(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "seriesId", required = false) String seriesId) {
		User user = UserSessionManager.getSessionUser();
		Map<String, Object> param = new HashMap<String, Object>();
		if (UserRole.INTERNAL_USER.getValue().equals(user.getRoleId())) {
			param.put("siteId", user.getSiteId());
		}
		if (status != null) {
			param.put("status", status);
		}
		if (seriesId != null) {
			param.put("seriesId", seriesId);
		}
		return eventDao.search(param);
	}
	
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}/video", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE }) 
	public void setVideoLink(@RequestParam String videoLink, @PathVariable String eventId, @PathVariable String attendeeId){
		attendeeDao.setVideoLink(attendeeId, videoLink);
	}

	/**
	 * Create Event 1. create attendees with seq 2. create event
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Event createEvent(@RequestBody Event event) {
		event.setId(CommonUtils.getRandomUUID());
		Event retEvent = eventDao.createEvent(event);
		// create attendees
		String eventId = retEvent.getId();
		int quota = retEvent.getQuota();
		int p = 1;
		while (p <= quota) {
			Attendee att = new Attendee();
			att.setId(CommonUtils.getRandomUUID());
			att.setEventId(eventId);
			att.setSeq(p);
			attendeeDao.create(att);
			p++;
		}
		return retEvent;
	}

	/*
	 * @ApiScope(userScope = UserRole.ADMIN)
	 * 
	 * @RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}",
	 * method = RequestMethod.POST, consumes = {
	 * MediaType.APPLICATION_JSON_VALUE }) public Attendee
	 * updateAttendee(@RequestBody Attendee attendee) { if(attendee.getScore() >
	 * 0) { if(Strings.isNullOrEmpty(attendee.getRoundId())) { // update the
	 * current attendee if admin did not choose any round
	 * 
	 * } } }
	 */

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/events/{eventId}/promote", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Attendee promote(@PathVariable String eventId, @RequestBody Attendee attendee) {
		if (Strings.isNullOrEmpty(attendee.getRoundId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.ROUND_ID_CAN_NOT_BE_NULL);
		}

		if (attendee.getScore() == 0 || attendee.getStatus() != 2) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.SCORE_CAN_NOT_BE_NULL);
		}

		if (!Strings.isNullOrEmpty(attendee.getNextRoundId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_PROMOTE);
		}

		List<Attendee> attendees = attendeeDao.getAllPendingAttendees(eventId);
		Event event = eventDao.getEvent(eventId);
		int quota = event.getQuota();
		int rt = quota >= attendees.size() ? attendees.size() : quota;
		Attendee att = null;
		for (int i = 0; i < rt; i++) {
			int num = r.nextInt(rt);
			att = attendees.get(num);

			if (att != null) {
				break;
			}
		}

		if (att == null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_QUOTA_ENROLL_MESSAGE);
		}

		Attendee toPromoteAttendee = createPromoteAttendee(attendee, att);
		attendee.setNextRoundId(attendee.getRoundId());
		attendeeDao.updateNextRound(attendee);
		attendeeDao.update(toPromoteAttendee);

		return toPromoteAttendee;
	}

	private Attendee createPromoteAttendee(Attendee attendee, Attendee promotedAttendee) {
		promotedAttendee.setAttendeeNotifyTimes(attendee.getAttendeeNotifyTimes());
		promotedAttendee.setAvatarUrl(attendee.getAvatarUrl());
		promotedAttendee.setEventId(attendee.getEventId());
		promotedAttendee.setKidId(attendee.getKidId());
		promotedAttendee.setKidName(attendee.getKidName());
		promotedAttendee.setSchoolName(attendee.getSchoolName());
		promotedAttendee.setSchoolType(attendee.getSchoolType());
		promotedAttendee.setRank(0);
		promotedAttendee.setRoundId(attendee.getRoundId());
		Round round = roundDao.get(attendee.getRoundId());
		promotedAttendee.setRoundLevel(round.getLevel());
		promotedAttendee.setRoundLevelName(round.getLevelName());
		promotedAttendee.setRoundShortName(round.getShortName());
		promotedAttendee.setSeq(attendee.getSeq());
		promotedAttendee.setRank(0);
		promotedAttendee.setStatus(1);
		promotedAttendee.setTagId(attendee.getTagId());
		promotedAttendee.setTag(attendee.getTag());
		promotedAttendee.setTeamId(attendee.getTeamId());
		promotedAttendee.setUserId(attendee.getUserId());
		return promotedAttendee;
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events/{eventId}/complete", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Attendee complete(@PathVariable String eventId, @RequestBody Attendee attendee) {
		List<Attendee> retAttendees = new ArrayList<>();
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
					tagId = NULL_TAG_ID;
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
		/*
		 * if (existeAttendeeMap.get(attendee.getId()) != null &&
		 * (existeAttendeeMap.get(attendee.getId()).getSeq() == null || !Objects
		 * .equals(existeAttendeeMap.get(attendee.getId()).getTagId(),
		 * attendee.getTagId()))) { String tagId = attendee.getTagId(); if
		 * (tagId == null) { tagId = NULL_TAG_ID; } String oldTagId =
		 * existeAttendeeMap.get(attendee.getId()).getTagId(); if (oldTagId ==
		 * null) { oldTagId = NULL_TAG_ID; } if (!groupMap.containsKey(tagId)) {
		 * groupMap.put(tagId, new TreeSet<Integer>()); } if
		 * (groupMap.containsKey(oldTagId) &&
		 * existeAttendeeMap.get(attendee.getId()).getSeq() != null) {
		 * groupMap.get(oldTagId).remove(existeAttendeeMap.get(attendee.getId())
		 * .getSeq()); } Integer seq = getSeqNumber(groupMap, tagId);
		 * attendee.setSeq(seq); groupMap.get(tagId).add(seq); }
		 */
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

	private Integer getSeqNumber(Map<String, Set<Integer>> groupMap, String tagId) {
		Set<Integer> seqs = groupMap.get(tagId);
		for (int i = 1; i <= attendeePerGourpLimitation; i++) {
			if (!seqs.contains(i)) {
				return i;
			}
		}
		throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_GROUP_QUOTA_ERROR_MESSAGE);
	}

	/**
	 * Enroll user into one event The attendee is mirror of registered user
	 * FIXME, we need a distribute lock to lock the quota.
	 * 
	 * @RequestMapping(value = "/events/{eventId}/enroll")
	 */
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/events/{eventId}/enroll", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Attendee enroll(@PathVariable String eventId, @RequestBody EnrollAttendee enrollAttendee,
			@RequestParam(required = false) String teamId) {
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
		
		
		if(returnKid.getFirstTimeAttendEvent() == 0) {
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

	/**
	 * 
	 * Update Event
	 * 
	 * @RequestMapping(value = "/events/{eventId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Event updateEvent(@PathVariable String eventId, @RequestBody Event event) {
		isEventValid(event);

		Event eventInDb = eventDao.getEvent(event.getId());
		if (alreadyComplete(eventInDb)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EVENT_ALREADY_COMPLETE);
		}
		Event retEvent = eventDao.updateEvent(event);
		List<Attendee> attendees = attendeeDao.getAttendeeFromEvent(retEvent.getId());
		for (Attendee attendee : attendees) {
			if (!Strings.isNullOrEmpty(attendee.getTagId())) {
				attendee.setTag(tagDao.getTag(attendee.getTagId()).getName());
			}
		}
		retEvent.setAttendees(attendees);
		return retEvent;
	}

	private boolean alreadyComplete(Event event) {
		return event.getStatus() >= 2;
	}

	/**
	 * Delete Event
	 * 
	 * @ApiScope(userScope = UserRole.INTERNAL_USER)
	 * @RequestMapping(value = "/events/{eventId}", method =
	 *                       RequestMethod.DELETE) public void
	 *                       delete(@PathVariable String eventId) {
	 *                       eventDao.delete(eventId); }
	 */

	/**
	 * 
	 * Get Event
	 * 
	 * @RequestMapping(value = "/events/{eventId}")
	 */
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
	public Event getEvent(@PathVariable String eventId) {
		Event retEvent = eventDao.getFullEvent(eventId);
		List<Attendee> newAttendees = new ArrayList<Attendee>();
		List<Attendee> attendees = retEvent.getAttendees();
		// List<Attendee> attendees =
		// attendeeDao.getAttendeeFromEvent(retEvent.getId());
		for (Attendee attendee : attendees) {
			if (!Strings.isNullOrEmpty(attendee.getId())) {
				newAttendees.add(attendee);
			}
		}
		retEvent.setAttendees(newAttendees);
		/*
		 * List<Attendee> attendees =
		 * attendeeDao.getAttendeeFromEvent(retEvent.getId()); for (Attendee
		 * attendee : attendees) { if
		 * (!Strings.isNullOrEmpty(attendee.getTagId())) {
		 * attendee.setTag(tagDao.getTag(attendee.getTagId()).getName()); }
		 * 
		 * if (!Strings.isNullOrEmpty(attendee.getKidId())) {
		 * attendee.setKidName(kidDao.get(attendee.getKidId()).getName()); } }
		 * retEvent.setAttendees(attendees);
		 */
		/*
		 * List<Attendee> attendees =
		 * attendeeDao.getAttendeeFromEvent(retEvent.getId()); for(Attendee
		 * attendee : attendees) { Kid kid = kidDao.get(attendee.getKidId());
		 * attendee.setKidName(kid.getName()); }
		 * retEvent.setAttendees(attendees);
		 */
		return retEvent;
	}

	private boolean isEventValid(Event event) {
		if (Strings.isNullOrEmpty(event.getId()) || Strings.isNullOrEmpty(event.getSiteId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_EVENT_FORMAT);
		}
		return true;
	}

	/**
	 * Set the attendee rank of the event and score
	 *
	 * @RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}")
	 */

}
