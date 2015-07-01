package com.smartool.common.dto;

import java.util.List;

public class Event {
	
	String eventId;
	
	String eventName;
	
	long eventCreatTime;
	
	int eventAttendeesQuota;
	
	List<Attendee> attendees;
	
	int eventType; //enum
	
	int siteId; //enum
}
