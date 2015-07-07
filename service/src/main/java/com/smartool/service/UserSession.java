package com.smartool.service;

public class UserSession {
	private String userId;
	private long lastActivateTime;

	public UserSession(String userId, long lastActivateTime) {
		this.userId = userId;
		this.lastActivateTime = lastActivateTime;
	}

	public long getLastActivateTime() {
		return lastActivateTime;
	}

	public void setLastActivateTime(long lastActivateTime) {
		this.lastActivateTime = lastActivateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
