package com.smartool.common.dto;

public class EventType extends BaseDateTrackingBean {
	
	private String id;
	
	private String name;
	
	private String siteId;
	
	private int eventSerieDefId;
	
	private String eventSerieDefName;

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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public int getEventSerieDefId() {
		return eventSerieDefId;
	}

	public void setEventSerieDefId(int eventSerieDefId) {
		this.eventSerieDefId = eventSerieDefId;
	}

	public String getEventSerieDefName() {
		return eventSerieDefName;
	}

	public void setEventSerieDefName(String eventSerieDefName) {
		this.eventSerieDefName = eventSerieDefName;
	}
}
