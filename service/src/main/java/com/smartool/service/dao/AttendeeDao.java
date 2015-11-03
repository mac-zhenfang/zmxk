package com.smartool.service.dao;

import java.util.List;
import java.util.Map;

import com.smartool.common.dto.Attendee;

public interface AttendeeDao {
	
	Attendee create(Attendee attendee);
	
	List<Attendee> getAttendeeFromEvent(String eventId);
	
	Attendee updateNotifyTimes(Attendee attendee);
	
	Attendee enroll(Attendee attendee);
	
	Attendee complete(Attendee attendee);
	
	Attendee getAttendee(String attendeeId);
	
	List<Attendee> getAllPendingAttendees(String eventId);

	void removeUnused(String eventId);
	
	Attendee update(Attendee attendee);
	
	Attendee updateNextRound(Attendee attendee);
	
	Map<String, Attendee> getAttendeesFromEvent(String eventId, String roundId);
	
	List<Attendee> listRoundsByLevelName(int level, String shortName, String eventId);
}
