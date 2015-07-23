package com.smartool.common.dto;

public class CreditRule extends BaseDateTrackingBean {
	private String id;
	private String name;
	private int credit;
	private int goldenMedal;
	private int silverMedal;
	private int bronzeMedal;

	public int getGoldenMedal() {
		return goldenMedal;
	}

	public void setGoldenMedal(int goldenMedal) {
		this.goldenMedal = goldenMedal;
	}

	public int getSilverMedal() {
		return silverMedal;
	}

	public void setSilverMedal(int silverMedal) {
		this.silverMedal = silverMedal;
	}

	public int getBronzeMedal() {
		return bronzeMedal;
	}

	public void setBronzeMedal(int bronzeMedal) {
		this.bronzeMedal = bronzeMedal;
	}

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

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
}
