package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Event;

public interface EventDao {

	List<Event> listAllEvent();

	Event createEvent(Event event);

	Event updateEvent(Event event);
}
