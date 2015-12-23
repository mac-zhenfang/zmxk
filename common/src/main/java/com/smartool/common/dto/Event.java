package com.smartool.common.dto;

import java.util.Date;
import java.util.List;

public class Event extends BaseDateTrackingBean {

	private String id;

	private String name;

	private Date eventTime;

	private int quota;
	
	private int eventSeq;

	private List<Attendee> attendees;

	private String eventTypeId;

	private String eventType;

	private String siteId; // enum

	private String seriesId;
	
	private String eventDefName;
	
	private String eventShortName;

	private int status;

	private int stage;
		
	private boolean isTeam;
	
	private boolean isSerieTeam;
	
	private boolean isEventTeam;
	
	public enum EventStatus {
		PREPARE(0), START(1), COMPLETE(2), FROZEN(3);
		private int value;

		EventStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public boolean isTeam() {
		return isSerieTeam | isEventTeam;
	}

	public void setTeam(boolean isTeam) {
		this.isTeam = isTeam;
	}

	public boolean isSerieTeam() {
		return isSerieTeam;
	}

	public void setSerieTeam(boolean isSerieTeam) {
		this.isSerieTeam = isSerieTeam;
	}

	public boolean isEventTeam() {
		return isEventTeam;
	}

	public void setEventTeam(boolean isEventTeam) {
		this.isEventTeam = isEventTeam;
	}

	public String getEventDefName() {
		return eventDefName;
	}

	public void setEventDefName(String eventDefName) {
		this.eventDefName = eventDefName;
	}

	public String getEventShortName() {
		return eventShortName;
	}

	public void setEventShortName(String eventShortName) {
		this.eventShortName = eventShortName;
	}

	public int getSeq() {
		return eventSeq;
	}

	public void setSeq(int seq) {
		this.eventSeq = seq;
	}
}
