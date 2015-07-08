package com.smartool.service;

public class Constants {
	public static final long SECURITY_CODE_INTERVAL = 30l * 1000;
	public static final long SECURITY_CODE_LIFE_SPAN = 24l * 60 * 60 * 1000;
	public static final int SESSION_AGE = 24 * 60 * 60 * 1000;
	public static final String KEY_FOR_USER_TOKEN = "zmxk_user_token";
	public static final String GET_SECURITY_CODE_PATH = "/users/code";
	public static final String USER_REGISTER_PATH = "/users/register";
	public static final String USER_LOGIN_PATH = "/users/login";
}
