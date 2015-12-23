package com.smartool.service.service;

import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.EnrollAttendee;

public interface EventService {
	
	public Attendee complete(Attendee attendee, String eventId);
	
	public Attendee enroll(String eventId, EnrollAttendee enrollAttendee, String teamId);
	
	public void batchComplete(String eventId, byte[] binaryFile);
}
