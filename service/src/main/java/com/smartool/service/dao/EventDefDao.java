package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.EventDef;

public interface EventDefDao {
	
	public List<EventDef> listEventDefBySerieId(String serieId);
}
