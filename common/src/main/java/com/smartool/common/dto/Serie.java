package com.smartool.common.dto;

import java.util.Date;

public class Serie {
	private String id;
	private String name;
	private Date startTime;
	private Date endTime;
	private int stages;
	private int rankUpgradeQualification;
	private boolean isTeam;
	private String eventTypeId;
	private int eventSerieDefId;

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getStages() {
		return stages;
	}

	public void setStages(int stages) {
		this.stages = stages;
	}

	public int getRankUpgradeQualification() {
		return rankUpgradeQualification;
	}

	public void setRankUpgradeQualification(int rankUpgradeQualification) {
		this.rankUpgradeQualification = rankUpgradeQualification;
	}


	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public boolean isTeam() {
		return isTeam;
	}

	public void setTeam(boolean isTeam) {
		this.isTeam = isTeam;
	}

	public int getEventSerieDefId() {
		return eventSerieDefId;
	}

	public void setEventSerieDefId(int eventSerieDefId) {
		this.eventSerieDefId = eventSerieDefId;
	}
}
