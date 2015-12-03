package com.smartool.common.dto;

public class Trophy implements Achievement {
	
	
	private int stage;
	
	private int roundLevel;
		
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}



	@Override
	public Type getType() {
		return Type.trophy;
	}
}

