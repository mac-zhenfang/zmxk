package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.User;

public class MockedUserDaoImpl implements UserDao {

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
//		user.setId()
		return user;
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidSecurityCode(String mobileNumber, String securityCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String generateSecurityCode(String mobileNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
