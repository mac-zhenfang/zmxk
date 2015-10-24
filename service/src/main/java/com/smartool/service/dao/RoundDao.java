package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Round;

public interface RoundDao {
	
	public List<Round> listAll();
	
	public List<Round> listAll(Integer level);
	
	public Round get(String roundId);
	
	public List<Round> listGroupByLevel();
}
