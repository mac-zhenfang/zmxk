package com.smartool.common.dto;

import java.util.Date;

public class Grade {
	
	private int score;
	
	private int rank;
	
	private int credit;
	
	private String eventTypeName;
	
	private String eventName;
	
	private String serieName;
	
	private Date eventTime;
	
	private String eventTypeId;
			
	private String kidName;
	
	private String userName;
	
	private String goldenMedal;
	private String silverMedal;
	private String bronzeMedal;

	public String getGoldenMedal() {
		return goldenMedal;
	}

	public void setGoldenMedal(String goldenMedal) {
		this.goldenMedal = goldenMedal;
	}

	public String getSilverMedal() {
		return silverMedal;
	}

	public void setSilverMedal(String silverMedal) {
		this.silverMedal = silverMedal;
	}

	public String getBronzeMedal() {
		return bronzeMedal;
	}

	public void setBronzeMedal(String bronzeMedal) {
		this.bronzeMedal = bronzeMedal;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getKidName() {
		return kidName;
	}

	public void setKidName(String kidName) {
		this.kidName = kidName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getSerieName() {
		return serieName;
	}

	public void setSerieName(String serieName) {
		this.serieName = serieName;
	}
	
}
