package com.smartool.common.dto;

public class CreditRecord extends BaseDateTrackingBean {
	private String id;
	private String userId;
	private String attendeeId;
	private String mobileNum;
	private String eventId;
	private CreditRuleType creditRuleType;
	private String creditRuleId;
	private String displayName;
	private Integer rank;
	private float score;
	private Integer credit;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CreditRuleType getCreditRuleType() {
		return creditRuleType;
	}

	public void setCreditRuleType(CreditRuleType creditRuleType) {
		this.creditRuleType = creditRuleType;
	}

	public String getCreditRuleId() {
		return creditRuleId;
	}

	public void setCreditRuleId(String creditRuleId) {
		this.creditRuleId = creditRuleId;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}
