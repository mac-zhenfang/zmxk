package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.EnrollAttendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.Kid;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.KidDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventController extends BaseController {

	@Autowired
	private EventDao eventDao;

	@Autowired
	private AttendeeDao attendeeDao;

	@Autowired
	private KidDao kidDao;

	private static Random r = new Random();

	/**
	 * List
	 * 
	 * @RequestMapping(value = "/events")
	 */
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public List<Event> getEvents() {
		List<Event> returnList = new ArrayList<Event>();
		returnList = eventDao.listAllEvent();
		return returnList;
	}

	/**
	 * Create Event 1. create attendees with seq 2. create event
	 */
	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Event createEvent(@RequestBody Event event) {
		event.setId(CommonUtils.getRandomUUID());
		Event retEvent = eventDao.createEvent(event);
		// create attendees
		String eventId = retEvent.getId();
		int quota = retEvent.getQuota();
		int p = 1;
		while (p++ <= quota) {
			Attendee att = new Attendee();
			att.setId(CommonUtils.getRandomUUID());
			att.setEventId(eventId);
			att.setSeq(p);
			attendeeDao.create(att);
		}
		return retEvent;
	}

	/**
	 * Enroll user into one event The attendee is mirror of registered user
	 * FIXME, we need a distribute lock to lock the quota.
	 * 
	 * @RequestMapping(value = "/events/{eventId}/enroll")
	 */
	@RequestMapping(value = "/events/{eventId}/enroll", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Attendee enroll(@PathVariable String eventId, @RequestBody EnrollAttendee enrollAttendee) {

		List<Attendee> attendees = attendeeDao.getAllPendingAttendees(eventId);
		Event event = eventDao.getEvent(eventId);
		Kid returnKid = null;
		String givenKidId = enrollAttendee.getKidId();
		if (enrollAttendee.getKid() != null && Strings.isNullOrEmpty(enrollAttendee.getKidId())) {
			Kid kid = enrollAttendee.getKid();
			kid.setId(CommonUtils.getRandomUUID());
			returnKid = kidDao.create(kid);
			givenKidId = returnKid.getId();
		}

		int quota = event.getQuota();
		List<Attendee> enrolledAttendees = attendeeDao.getAttendeeFromEvent(eventId);
		
		if(exceedQuota(quota, enrolledAttendees)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.EXCEED_QUOTA_ENROLL_MESSAGE);
		}
		
		if(isDupliatedEnrolled(enrolledAttendees, givenKidId)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_ENROLL_MESSAGE); 
		}
		return internalEnroll(enrollAttendee, attendees, returnKid, eventId, quota, quota);
	}
	
	private boolean exceedQuota(int quota, List<Attendee> enrolledAttendees){
		return quota == enrolledAttendees.size();
	}
	
	private boolean isDupliatedEnrolled(List<Attendee> attendees, String inputKidId) {
		
		for(Attendee attendee : attendees) {
			String kidId = attendee.getKidId();
			if(!Strings.isNullOrEmpty(kidId) && kidId.equals(inputKidId)) {
				return true;
			}
		}
		return false;
	}

	private Attendee internalEnroll(EnrollAttendee enrollAttendee, List<Attendee> attendees, Kid returnKid, String eventId, int retryTimes, int quota) {

		for (int i = 0; i < retryTimes; i++) {
			int num = r.nextInt(quota);
			Attendee attendee = attendees.get(num);
			attendee.setStatus(1); // change to enroll status
			// FIXME: check Kid/User/Event if valid
			attendee.setEventId(eventId);
			attendee.setUserId(enrollAttendee.getUserId());
			if (!Strings.isNullOrEmpty(enrollAttendee.getKidId())) {
				attendee.setKidId(enrollAttendee.getKidId());
			} else if (returnKid != null) {
				attendee.setKidId(returnKid.getId());
			} else {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_KID_SELECTION);
			}
			Attendee createdAttendee = attendeeDao.enroll(attendee);
			if (null != createdAttendee) {
				return createdAttendee;
			}
		}
		
		throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessages.WRONG_KID_SELECTION);
	}

	/**
	 * 
	 * Update Event
	 * 
	 * @RequestMapping(value = "/events/{eventId}")
	 */
	@Transactional
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Event updateEvent(@PathVariable String eventId, @RequestBody Event event) {
		isEventValid(event);
		Event retEvent = eventDao.updateEvent(event);
		List<Attendee> attendees = attendeeDao.getAttendeeFromEvent(retEvent.getId());
		retEvent.setAttendees(attendees);
		return retEvent;
	}

	/**
	 * 
	 * Get Event
	 * 
	 * @RequestMapping(value = "/events/{eventId}")
	 */
	@Transactional
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
	public Event getEvent(@PathVariable String eventId) {
		Event retEvent = eventDao.getFullEvent(eventId);
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
		if (Strings.isNullOrEmpty(event.getId()) || Strings.isNullOrEmpty(event.getSiteId())
				|| Strings.isNullOrEmpty(event.getEventTypeId())) {
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
