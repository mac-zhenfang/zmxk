package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.User;

public interface UserDao {
	User createUser(User user);

	User getUserById(String userId);

	User getUserFromWeChat(String mobileNumber);

	User updateUser(User user);

	List<User> listAllUser();

	boolean isValidSecurityCode(String mobileNumber, String securityCode);

	String generateSecurityCode(String mobileNumber);
}
