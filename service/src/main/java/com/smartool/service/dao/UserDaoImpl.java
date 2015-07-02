package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.User;

public class UserDaoImpl implements UserDao{
	@Autowired
	SqlSession sqlSession;

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserFromWeChat(String mobileNumber) {
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
		return sqlSession.selectList("USER.listAll");
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
