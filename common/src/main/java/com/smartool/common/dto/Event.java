package com.smartool.common.dto;

import java.util.Date;
import java.util.List;

public class Event extends BaseDateTrackingBean {
	
	private String eventId;
	
	private String eventName;
	
	private Date eventTime;
		
	private int quota;
	
	private List<Attendee> attendees;
	
	private String eventsTypeId;
	
	private String eventsType;
	
	private int siteId; //enum

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

	public List<Attendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<Attendee> attendees) {
		this.attendees = attendees;
	}

	public String getEventsTypeId() {
		return eventsTypeId;
	}

	public void setEventsTypeId(String eventsTypeId) {
		this.eventsTypeId = eventsTypeId;
	}

	public String getEventsType() {
		return eventsType;
	}

	public void setEventType(String eventType) {
		this.eventsType = eventType;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
}
