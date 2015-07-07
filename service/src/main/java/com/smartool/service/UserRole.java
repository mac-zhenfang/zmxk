package com.smartool.service;

public enum UserRole {
	NORMAL_USER("0"), ADMIN("1");
	private String value;

	UserRole(String roleId) {
		value = roleId;
	}

	public String getValue() {
		return value;
	}
}
