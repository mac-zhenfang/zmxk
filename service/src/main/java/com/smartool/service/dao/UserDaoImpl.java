package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;

public class UserDaoImpl implements UserDao {
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private KidDao kidDao;

	private User getUserInternal(String userId) {
		User user = sqlSession.selectOne("USER.getById", userId);
		if (user != null) {
			List<Kid> kids = kidDao.listByUserId(userId);
			user.setKids(kids);
		}
		return user;
	}

	@Override
	public User createUser(User user) {
		sqlSession.insert("USER.create", user);
		if (user.getKids() != null && !user.getKids().isEmpty()) {
			for (Kid kid : user.getKids()) {
				kid.setId(CommonUtils.getRandomUUID());
				kid.setUserId(user.getId());
				kidDao.create(kid);
			}
		}
		return getUserInternal(user.getId());
	}

	@Override
	public User getUserById(String userId) {
		return getUserInternal(userId);
	}

	@Override
	public User updateUser(User user) {
		sqlSession.insert("USER.update", user);
		return getUserInternal(user.getId());
	}

	@Override
	public List<User> listAllUser() {
		List<User> users = sqlSession.selectList("USER.listAll");
		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				List<Kid> kids = kidDao.listByUserId(user.getId());
				user.setKids(kids);
			}
		}
		return users;
	}

	@Override
	public User getUserByMobileNumber(String mobileNumber) {
		User user = sqlSession.selectOne("USER.getByMobileNumber", mobileNumber);
		if (user != null) {
			List<Kid> kids = kidDao.listByUserId(user.getId());
			user.setKids(kids);
		}
		return user;
	}

	@Override
	public List<User> search(String query) {
		List<User> users = sqlSession.selectList("USER.search", query);
		return users;
	}
}
