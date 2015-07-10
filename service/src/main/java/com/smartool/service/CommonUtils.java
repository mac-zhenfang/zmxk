package com.smartool.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import org.springframework.web.method.HandlerMethod;

import com.smartool.service.controller.annotation.ApiScope;

public class CommonUtils {
	private static int securityCodeLength = 6;
	private static MessageDigest messageDigest;

	static {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
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
		byte[] digest = messageDigest.digest(string.getBytes());
		return new String(digest);
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
