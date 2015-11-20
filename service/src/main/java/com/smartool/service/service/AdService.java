package com.smartool.service.service;

import java.util.List;

import com.smartool.common.dto.Cover;

public interface AdService {
	
	public List<Cover> insertAds(List<Cover> covers, String userId);
}
