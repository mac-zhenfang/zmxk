package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Tag;

public interface TagDao {
	
	public Tag getTag(String tagId);
	
	public List<Tag> getTags(String type);
	
	public List<Tag> getTags();
	
	public Tag create(Tag tag);
	
	public Tag update(Tag tag);
} 
