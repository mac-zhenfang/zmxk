package com.smartool.service.service;

import java.io.InputStream;

import com.smartool.common.dto.Avatar;

public interface ImageService {
	
	public Avatar upload(String userId, String kidId, InputStream image);
	
	public Avatar uploadCover(String userId, String kidId, InputStream image);

}
