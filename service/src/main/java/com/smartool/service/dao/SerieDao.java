package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Serie;

public interface SerieDao {
	
	public List<Serie> list(String eventTypeId);
	
	public List<Serie> list();
	
	public Serie get(String id);
	
	public Serie update(Serie serie);
	
	public void delete(String id);
	
	public void batchDeleteByEventType(String eventTypeId);

	public Serie create(Serie serie);
}
