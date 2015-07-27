package com.smartool.common.dto;

import java.util.Date;

public class BaseGrade {

	private int score;

	private int rank;

	private int stage;
	
	private String userId;

	private String eventTypeName;

	private String eventName;
	
	private String kidId;

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

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKidId() {
		return kidId;
	}

	public void setKidId(String kidId) {
		this.kidId = kidId;
	}

}
