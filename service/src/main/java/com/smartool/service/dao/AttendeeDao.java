package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Attendee;

public interface AttendeeDao {
	
	Attendee create(Attendee attendee);
	
	List<Attendee> getAttendeeFromEvent(String eventId);
	
	Attendee update(Attendee attendee);
	
	Attendee enroll(Attendee attendee);
	
	Attendee complete(Attendee attendee);
	
	Attendee getAttendee(String attendeeId);
	
	List<Attendee> getAllPendingAttendees(String eventId);
}
