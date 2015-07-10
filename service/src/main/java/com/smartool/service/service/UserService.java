package com.smartool.service.service;

import java.util.List;

import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;

public interface UserService {
	List<User> listAllUser();

	User login(SecurityCode securityCode, User user);

	User register(SecurityCode securityCode, User user);

	User getUserById(String userId);

	byte[] getQRCode(String userId);
}
