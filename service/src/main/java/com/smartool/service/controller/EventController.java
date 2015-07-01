package com.smartool.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventController {
	
	/**
	 * Create/List
	 * */
	@RequestMapping(value = "/events")
	/**
	 * 
	 * */
	@RequestMapping(value = "/events/{eventId}")
	/**
	 * Enroll user into one event
	 * The attendee is mirror of registered user
	 * */
	@RequestMapping(value = "/events/{eventId}/attendees")
	/**
	 * Set the attendee rank of the event and score
	 * */
	@RequestMapping(value = "/events/{eventId}/attendees/{attendeeId}")
	
	
}
