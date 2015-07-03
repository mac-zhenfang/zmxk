package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.User;

public interface UserDao {
	User createUser(User user);

	User getUserById(String userId);

	User updateUser(User user);

	List<User> listAllUser();
}
