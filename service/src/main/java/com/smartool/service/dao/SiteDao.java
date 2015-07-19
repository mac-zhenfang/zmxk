package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Site;

public interface SiteDao {
	
	public List<Site> list();
	
	public Site get(String id);
	
	public Site update(Site Site);
	
	public void delete(String SiteId);
	
	public Site create(Site Site);
}
