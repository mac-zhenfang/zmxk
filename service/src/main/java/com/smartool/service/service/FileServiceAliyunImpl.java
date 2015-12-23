package com.smartool.service.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.smartool.common.dto.Avatar;
import com.smartool.service.SmartoolException;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.KidDao;

public class FileServiceAliyunImpl implements FileService {

	@Autowired
	private SmartoolServiceConfig config;

	@Autowired
	private OSSClient ossClient;

	@Autowired
	KidDao kidDao;

	private final static String SEPERATOR = "/";

	private final static String FILE_SEPEATOR = ".";

	private final static String PROTOCOL = "http://";
	private final static String PARAMETER_SEPERATOR = "?";
	private final static String PARAMETER_TIMESTAMP = "ts";
	private final static String PARAMETER_EQUAL = "=";

	@Override
	public Avatar upload(String userId, String kidId, InputStream image) {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(image.available());
			objectMeta.setContentType(MediaType.IMAGE_JPEG_VALUE);
			// objectMeta.setContentMD5(contentMD5);
			PutObjectResult result = ossClient.putObject(config.getAvatarBucket(), generateOssFileName(kidId, config.getAvatarFileFormatSuffix()), image,
					objectMeta);
			String avatarUrl = generateAvatarUrl(kidId);
			updateAvatarUrl(kidId, avatarUrl);
			Avatar avatar = new Avatar();
			avatar.setUrl(avatarUrl);
			return avatar;
		} catch (Exception e) {
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error when upload avatar", e);
		}
	}
	
	@Override
	public Avatar uploadCover(String userId, String kidId, InputStream image) {
		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(image.available());
			objectMeta.setContentType(MediaType.IMAGE_JPEG_VALUE);
			// objectMeta.setContentMD5(contentMD5);
			PutObjectResult result = ossClient.putObject(config.getCoverBucket(), generateOssFileName(kidId, config.getCoverFileFormatSuffix()), image,
					objectMeta);
			String coverUrl = generateCoverUrl(kidId);
			kidDao.updateCoverUrl(kidId, coverUrl);
			Avatar avatar = new Avatar();
			avatar.setUrl(coverUrl);
			return avatar;
		} catch (Exception e) {
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error when upload avatar", e);
		}
	}

	private void updateAvatarUrl(String kidId, String avatarUrl) {
		kidDao.updateAvatarUrl(kidId, avatarUrl);
	}
	
	private String generateCoverUrl(String kidId) {
		StringBuilder sb = new StringBuilder();
		sb.append(PROTOCOL).append(config.getAvatarCName()).append(SEPERATOR).append(kidId).append(FILE_SEPEATOR)
				.append(config.getCoverFileFormatSuffix()).append(PARAMETER_SEPERATOR).append(PARAMETER_TIMESTAMP)
				.append(PARAMETER_EQUAL).append(System.currentTimeMillis());
		return sb.toString();
	}
	private String generateAvatarUrl(String kidId) {
		StringBuilder sb = new StringBuilder();
		sb.append(PROTOCOL).append(config.getAvatarCName()).append(SEPERATOR).append(kidId).append(FILE_SEPEATOR)
				.append(config.getAvatarFileFormatSuffix()).append(PARAMETER_SEPERATOR).append(PARAMETER_TIMESTAMP)
				.append(PARAMETER_EQUAL).append(System.currentTimeMillis());
		return sb.toString();
	}
	
	private String generateExcelUrl(String eventId) {
		StringBuilder sb = new StringBuilder();
		sb.append(PROTOCOL).append(config.getFileCName()).append(SEPERATOR).append(eventId).append(FILE_SEPEATOR)
				.append(config.getCsvFileFormatSuffix()).append(PARAMETER_SEPERATOR).append(PARAMETER_TIMESTAMP)
				.append(PARAMETER_EQUAL).append(System.currentTimeMillis());
		return sb.toString();
	}

	private String generateOssFileName(String kidId, String suffix) {
		StringBuilder sb = new StringBuilder();
		sb.append(kidId).append(FILE_SEPEATOR).append(suffix);
		return sb.toString();
	}

	@Override
	public String uploadExcel(String eventId, InputStream file) {

		try {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(file.available());
			objectMeta.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			// objectMeta.setContentMD5(contentMD5);
			PutObjectResult result = ossClient.putObject(config.getCoverBucket(), generateOssFileName(eventId, config.getCsvFileFormatSuffix()), file,
					objectMeta);
			String coverUrl = generateExcelUrl(eventId);
			
			return coverUrl;
		} catch (Exception e) {
			throw new SmartoolException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error when upload avatar", e);
		}
	
	}

	@Override
	public InputStream getExcel(String eventId, String url) {
		OSSObject object = ossClient.getObject(config.getCoverBucket(), url);
		return object.getObjectContent();
	}
}
