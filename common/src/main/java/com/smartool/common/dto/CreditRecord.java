package com.smartool.common.dto;

public class CreditRecord extends BaseDateTrackingBean {
	private String id;
	private String userId;
	private CreditRuleType creditRuleType;
	private String creditRuleId;

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
}
