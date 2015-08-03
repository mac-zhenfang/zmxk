package com.smartool.common.dto;

public class EventCreditRule extends CreditRule {
	private String eventTypeId;
	private String seriesId;
	private Integer upperRank;
	private Integer lowerRank;
	private Integer stage;

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getUpperRank() {
		return upperRank;
	}

	public void setUpperRank(Integer upperRank) {
		this.upperRank = upperRank;
	}

	public Integer getLowerRank() {
		return lowerRank;
	}

	public void setLowerRank(Integer lowerRank) {
		this.lowerRank = lowerRank;
	}

	@Override
	public String getRuleType() {
		return "GENERAL";
	}
}
