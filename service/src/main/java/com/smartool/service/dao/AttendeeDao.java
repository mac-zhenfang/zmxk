package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Attendee;

public interface AttendeeDao {
	
	Attendee enroll(Attendee attendee);
	
	List<Attendee> getAttendeeFromEvent(String eventId);
}
