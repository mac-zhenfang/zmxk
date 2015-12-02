package com.smartool.common.dto;

import com.smartool.common.dto.Cover.Type;

public interface Achievement {
	public Type getType();
	static enum Type {
		medal, trophy
	}
}
