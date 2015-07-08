package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


	/**
	 * List
	 * 
	 * @RequestMapping(value = "/events")
	 */
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public List<Event> getEvents() {
		List<Event> returnList = new ArrayList<Event>();
		returnList = eventDao.listAllEvent();
		for(Event event : returnList) {
			List<Attendee> attendees = attendeeDao.getAttendeeFromEvent(event.getId());
			event.setAttendees(attendees);
		}
		return returnList;
	}
	
	/**
	 * Create Event
	 * */
	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE })
	public Event createEvent(@RequestBody Event event) {
		event.setId(CommonUtils.getRandomUUID());
		return eventDao.createEvent(event);
	}
	/**
	 * Enroll user into one event The attendee is mirror of registered user
	 * 
	 * @RequestMapping(value = "/events/{eventId}/attendees")
	 */
	@RequestMapping(value = "/events/{eventId}/attendees", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Attendee enroll(@PathVariable String eventId, @RequestBody  EnrollAttendee enrollAttendee) {
		
		Attendee attendee = new Attendee();
		attendee.setId(CommonUtils.getRandomUUID());
		//FIXME: check Kid/User/Event if valid
		attendee.setEventId(eventId);
		attendee.setUserId(enrollAttendee.getUserId());
		if(!Strings.isNullOrEmpty(enrollAttendee.getKidId())) {
			attendee.setKidId(enrollAttendee.getKidId());
		} else if (enrollAttendee.getKid() !=null) {
			Kid kid = enrollAttendee.getKid();
			kid.setId(CommonUtils.getRandomUUID());
			Kid returnKid = kidDao.create(kid);
			attendee.setKidId(returnKid.getId());
		} else {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
						ErrorMessages.WRONG_KID_SELECTION);
		}
		Attendee createdAttendee = attendeeDao.enroll(attendee);
		return createdAttendee;
	}
	/**
	 * 
	 *
	 * @RequestMapping(value = "/events/{eventId}")
	 */

	/**
	 * Set the attendee rank of the event and score
	 *
	 * @RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}")
	 */

}
