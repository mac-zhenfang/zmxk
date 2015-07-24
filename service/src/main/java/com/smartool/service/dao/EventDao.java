package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Event;

public interface EventDao {

	List<Event> listAllEvent();
	
	List<Event> listAllEvent(String siteId);
	
	List<Event> listAllEvent (int status);
	
	List<Event> listFullEvent();

	Event createEvent(Event event);

	Event updateEvent(Event event);
	
	Event getEvent(String eventId);
	
	Event getFullEvent(String eventId);
	
	void delete(String eventId);
}
