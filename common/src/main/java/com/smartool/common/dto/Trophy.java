package com.smartool.common.dto;

public class Trophy implements Achievement {
	
	
	private int stage;
	
	private int roundLevel;
	
	private String roundLevelName;
	
	private String roundId;
	
	private int rank;

	

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getRoundLevel() {
		return roundLevel;
	}

	public void setRoundLevel(int roundLevel) {
		this.roundLevel = roundLevel;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRoundLevelName() {
		return roundLevelName;
	}

	public void setRoundLevelName(String roundLevelName) {
		this.roundLevelName = roundLevelName;
	}

	@Override
	public Type getType() {
		return Type.trophy;
	}
}

