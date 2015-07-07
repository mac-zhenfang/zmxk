package com.smartool.service;

import com.smartool.common.dto.User;

public class UserSessionManager {
	private static ThreadLocal<User> localUser = new ThreadLocal<User>();

	private UserSessionManager() {
	}

	public static void setSessionUser(User user) {
		localUser.set(user);
	}

	public static User getSessionUser() {
		return localUser.get();
	}
}
