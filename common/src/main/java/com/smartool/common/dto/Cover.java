package com.smartool.common.dto;

public interface Cover {
	public Type getType();
	static enum Type {
		ad, grade
	}
}
