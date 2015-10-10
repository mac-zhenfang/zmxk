package com.smartool.service.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.smartool.common.dto.Avatar;
import com.smartool.service.SmartoolException;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.KidDao;

public class AvatarServiceImpl implements AvatarService {

	@Autowired
	private SmartoolServiceConfig config;

	@Autowired
	private OSSClient ossClient;
	
	@Autowired
	KidDao kidDao;

	private final static String SEPERATOR = "/";

	private final static String FILE_SEPEATOR = ".";

	private final static String PROTOCOL = "http://";

	@Override
	public Avatar upload(String userId, String kidId, InputStream image) {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(image.available());
			objectMeta.setContentType(MediaType.IMAGE_JPEG_VALUE);
			// objectMeta.setContentMD5(contentMD5);
			PutObjectResult result = ossClient.putObject(config.getAvatarBucket(), generateOssFileName(kidId), image,
					objectMeta);
			String avatarUrl = generateAvatarUrl(kidId);
			updateAvatarUrl(kidId, avatarUrl);;
			Avatar avatar = new Avatar();
			avatar.setUrl(avatarUrl);
			return avatar;
		} catch (Exception e) {
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error when upload avatar", e);
		}
	}
	
	private void updateAvatarUrl(String kidId, String avatarUrl) {
		kidDao.updateAvatarUrl(kidId, avatarUrl);
	}

	private String generateAvatarUrl(String kidId) {
		StringBuilder sb = new StringBuilder();
		sb.append(PROTOCOL).append(config.getAvatarCName()).append(SEPERATOR).append(kidId).append(FILE_SEPEATOR)
				.append(config.getAvatarFileFormatSuffix());
		return sb.toString();
	}

	private String generateOssFileName(String kidId) {
		StringBuilder sb = new StringBuilder();
		sb.append(kidId).append(FILE_SEPEATOR).append(config.getAvatarFileFormatSuffix());
		return sb.toString();
	}

}
