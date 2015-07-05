package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Event;
import com.smartool.service.dao.EventDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventController extends BaseController {
	
	@Autowired
	private EventDao eventDao;
	/**
	 * Create/List
	 * 
	 * @RequestMapping(value = "/events")
	 */
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public List<Event> getEvents() {
		return eventDao.listAllEvent();
	}
	/**
	 * 
	 *
	 * @RequestMapping(value = "/events/{eventId}")
	 */
	/**
	 * Enroll user into one event The attendee is mirror of registered user
	 * 
	 * @RequestMapping(value = "/events/{eventId}/attendees")
	 */
	/**
	 * Set the attendee rank of the event and score
	 *
	 * @RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}")
	 */

}
