package com.smartool.service;

import java.util.Random;
import java.util.UUID;

public class CommonUtils {
	private static int securityCodeLength = 6;

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
}
