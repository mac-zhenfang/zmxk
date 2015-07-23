package com.smartool.common.dto;

public class CreditRecord extends BaseDateTrackingBean {
	private String id;
	private String userId;
	private String operatorId;
	private CreditRuleType creditRuleType;
	private String creditRuleId;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}
}
