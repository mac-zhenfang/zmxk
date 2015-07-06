package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.User;

public class UserDaoImpl implements UserDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public User createUser(User user) {
		sqlSession.insert("USER.create", user);
		return sqlSession.selectOne("USER.getById", user.getId());
	}

	@Override
	public User getUserById(String userId) {
		return sqlSession.selectOne("USER.getById", userId);
	}

	@Override
	public User updateUser(User user) {
		sqlSession.insert("USER.update", user);
		return sqlSession.selectOne("USER.getById", user.getId());
	}

	@Override
	public List<User> listAllUser() {
		return sqlSession.selectList("USER.listAll");
	}

	@Override
	public User getUserByMobileNumber(String mobileNumber) {
		return sqlSession.selectOne("USER.getByMobileNumber", mobileNumber);
	}

	@Override
	public User getUserByWcId(String wcId) {
		return sqlSession.selectOne("USER.getByWcId", wcId);
	}
}
