package com.smartool.common.dto;

public class EventDef extends BaseDateTrackingBean {
	
	private int id;
	
	private String name;
	
	private String shortName;
	
	private int stage;
	
	private int eventSerieDefId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getEventSerieDefId() {
		return eventSerieDefId;
	}

	public void setEventSerieDefId(int eventSerieDefId) {
		this.eventSerieDefId = eventSerieDefId;
	}

}
