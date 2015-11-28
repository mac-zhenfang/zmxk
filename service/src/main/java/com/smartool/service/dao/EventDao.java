package com.smartool.service.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.smartool.common.dto.Event;

public interface EventDao {

	List<Event> listAllEvent();
	
	List<Event> ListAllEvent(int status, Date start, Date end);
	
	List<Event> listAllEvent(String siteId);
	
	List<Event> listAllEvent (int status);
	
	List<Event> listFullEvent();

	Event createEvent(Event event);

	Event updateEvent(Event event);
	
	Event getEvent(String eventId);
	
	Event getFullEvent(String eventId);
	
	void delete(String eventId);

	List<Event> search(Map<String, Object> param);
	
	void backup(Event event);
	
	List<Event> listAllEvent(long interval);
	
	void removeFromHis(long inteval);
}
