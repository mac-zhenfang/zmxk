package com.smartool.service;

public class Constants {
	public static final long SECURITY_CODE_INTERVAL = 30l * 1000;
	public static final long SECURITY_CODE_LIFE_SPAN = 24l * 60 * 60 * 1000;
	public static final int COOKIE_AGE = 24 * 60 * 60 * 1000;
	public static final String COOKIE_PATH = "/";
	public static final String KEY_FOR_USER_TOKEN = "zmxk_user_token";
	public static final String USER_ID_KEY = "userId";
	public static final String GET_SECURITY_CODE_PATH = "/users/code";
	public static final String USER_REGISTER_PATH = "/users/register";
	public static final String USER_LOGIN_PATH = "/users/login";
	public static final int USE_DEFAULT_PASSWORD = 1;
	public static final int NOT_USE_DEFAULT_PASSWORD = 0;
	public final static String NULL_TAG_ID = "NULL_TAG_ID";
}
