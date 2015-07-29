package com.smartool.common.dto;

public enum EventStatus {
	PREPARE(0), START(1), COMPLETE(2);
	private int value;

	EventStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
