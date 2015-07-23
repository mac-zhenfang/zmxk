package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.Grade;
import com.smartool.common.dto.User;

public interface UserDao {
	User createUser(User user);

	User getUserById(String userId);

	User getUserByMobileNumber(String mobileNumber);

	User updateUser(User user);

	List<User> listAllUser();
	
	List<User> search(String query);
	
	void remove(String userId);
	
	void addCredit(String userId, int credit);
	
	List<Grade> getGrades(String userId);
}
