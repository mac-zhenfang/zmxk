package com.smartool.service;

public enum UserRole {
	NORMAL_USER("0"), INTERNAL_USER("1"), ADMIN("2");
	private String value;

	UserRole(String roleId) {
		value = roleId;
	}

	public String getValue() {
		return value;
	}
}
