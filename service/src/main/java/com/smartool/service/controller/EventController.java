package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.EnrollAttendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.Round;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.RoundDao;
import com.smartool.service.dao.TagDao;
import com.smartool.service.service.EventService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventController extends BaseController {
	

	private static Logger logger = Logger.getLogger(EventController.class);
	@Autowired
	private EventDao eventDao;

	@Autowired
	private AttendeeDao attendeeDao;
	
	@Autowired
	private EventService attendeeService;

	

	// @Autowired
	// private UserDao userDao;

	@Autowired
	private RoundDao roundDao;

	@Autowired
	private TagDao tagDao;

	private int attendeePerGourpLimitation = 100;

	private static Random r = new Random();

	


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
	public List<Event> getRecentEvents(
			@RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date start,
			@RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date end) {
		// 2015-09-19 08:00:00
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
	public void setVideoLink(@RequestParam String videoLink, @PathVariable String eventId,
			@PathVariable String attendeeId) {
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
		return attendeeService.complete(attendee, eventId);
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
		return attendeeService.enroll(eventId, enrollAttendee, teamId);
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
		
		return retEvent;
	}
	@RequestMapping(value = "/events/{eventId}/batch", method = RequestMethod.POST)
	public void batchComplete(@PathVariable String eventId,  @RequestParam("file") MultipartFile file) {
		
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
