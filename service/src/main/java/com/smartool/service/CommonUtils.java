package com.smartool.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;

import com.smartool.service.controller.annotation.ApiScope;

public class CommonUtils {
	private static Logger logger = Logger.getLogger(CommonUtils.class);
	private static int securityCodeLength = 6;
	private static MessageDigest messageDigest;
	private static Charset charset;

	static {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			charset = Charset.forName("UTF-8");
		} catch (Exception e) {
			logger.error("File to initialize: ", e);
		}
	}

	public static String getRandomUUID() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomSecurityCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < securityCodeLength; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public static boolean isEmptyString(String string) {
		if (string == null || string.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static String encryptBySha2(String string) {
		byte[] digest = messageDigest.digest(string.getBytes(charset));
		return new String(digest, charset);
	}

	public static boolean isSaveMethod(Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ApiScope apiScope = handlerMethod.getMethod().getAnnotation(ApiScope.class);
			return apiScope == null;
		} else {
			return false;
		}
	}
}
