package com.smartool.service.dao;

import java.util.List;
import java.util.Random;

import com.smartool.common.dto.User;

public class MockedUserDaoImpl implements UserDao {
	private static int securityCodeLength = 6;

	private String getRandomSecurityCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < securityCodeLength; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		// user.setId()
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
		return true;
	}

	@Override
	public String generateSecurityCode(String mobileNumber) {
		return getRandomSecurityCode();
	}

	@Override
	public User getUserFromWeChat(String mobileNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
