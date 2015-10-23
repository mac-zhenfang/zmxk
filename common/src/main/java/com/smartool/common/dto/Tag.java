package com.smartool.common.dto;

public class Tag extends BaseDateTrackingBean {
	private String id;
	
	private String name;
	
	private String type;
	
	
	private Integer level;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public enum TagType {
		event, eventGroup, user, site, eventType, serie
	}
}



