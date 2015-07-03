package com.smartool.service;

public class Constants {
	public static final long SECURITY_CODE_INTERVAL = 30l * 1000;
	public static final long SECURITY_CODE_LIFE_SPAN = 24l * 60 * 60 * 1000;
	public static final String KEY_FOR_USER_ID = "mac-test";
	public static final String FREQUENT_REQUEST_SECURITY_CODE_ERROR_MESSAGE = "获取验证码过于频繁";
}
