package com.smartool.service.service;

import java.io.InputStream;

import com.smartool.common.dto.Avatar;

public interface FileService {
	
	public Avatar upload(String userId, String kidId, InputStream image);
	
	public Avatar uploadCover(String userId, String kidId, InputStream image);
	
	public String uploadExcel(String eventId, InputStream file);
	
	public InputStream getExcel(String eventId, String url);

}
