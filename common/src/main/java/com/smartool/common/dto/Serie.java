package com.smartool.common.dto;

public class Serie {
	private String id;
	private String name;
	private String startTime;
	private String endTime;
	private int stage;
	private int rankUpgradeQualification;
	private String siteId;
	private String eventTypeId;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getRankUpgradeQualification() {
		return rankUpgradeQualification;
	}

	public void setRankUpgradeQualification(int rankUpgradeQualification) {
		this.rankUpgradeQualification = rankUpgradeQualification;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
}
