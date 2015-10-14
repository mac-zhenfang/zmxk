package com.smartool.common.dto;

public class EventSerieDef extends BaseDateTrackingBean {
	
	private int id;
	
	private String name;
	
	private int stages;

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

	public int getStages() {
		return stages;
	}

	public void setStages(int stages) {
		this.stages = stages;
	}
}
