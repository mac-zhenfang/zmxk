package com.smartool.common.dto;

public class EventCreditRule extends CreditRule {
	private String eventTypeId;
	private Integer upperRank;
	private Integer lowerRank;
	private int roundLevel;
	private Integer stage;
	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
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

	public int getRoundLevel() {
		return roundLevel;
	}

	public void setRoundLevel(int roundLevel) {
		this.roundLevel = roundLevel;
	}
}
