package com.smartool.common.dto;

import java.util.HashMap;
import java.util.Map;

public enum EventStages {
	PRELIMINARIES(1), SEASONAL(2), YEARLY(3);

	private static Map<Integer, String> eventStagesDisplayNameMap = new HashMap<Integer, String>();

	static {
		eventStagesDisplayNameMap.put(1, "A");
		eventStagesDisplayNameMap.put(2, "B");
		eventStagesDisplayNameMap.put(3, "C");
	}

	private int id;

	EventStages(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getDisplayName() {
		return eventStagesDisplayNameMap.get(id);
	}

	public static String getDisplayName(int id) {
		return eventStagesDisplayNameMap.get(id);
	}
}
