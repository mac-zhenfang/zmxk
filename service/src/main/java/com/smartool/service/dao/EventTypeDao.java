package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.EventType;

public interface EventTypeDao {
	
	public List<EventType> listBySite(String siteId);
	
	public List<EventType> list();
	
	public EventType get(String id);
	
	public EventType update(EventType eventType);
	
	public void delete(String eventTypeId);
	
	public EventType create(EventType eventType);
}
