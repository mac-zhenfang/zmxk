package com.smartool.service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.User;
import com.smartool.common.dto.UserGrade;
import com.smartool.common.dto.UserStat;
import com.smartool.service.CommonUtils;

public class UserDaoImpl implements UserDao {
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private KidDao kidDao;

	private User getUserInternal(String userId) {
		User user = sqlSession.selectOne("USER.getById", userId);

		return user;
	}

	@Override
	public User createUser(LoginUser user) {
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
		sqlSession.selectOne("USER.update", user);
		return getUserInternal(user.getId());
	}

	@Override
	public List<User> listAllUser() {
		List<User> users = sqlSession.selectList("USER.listAll");
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
	public List<User> search(String mobileNum, String wcId, String kidName) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (mobileNum != null) {
			param.put("mobileNum", mobileNum);
		}
		if (wcId != null) {
			param.put("wcId", wcId);
		}
		if (kidName != null) {
			param.put("kidName", kidName);
		}
		return sqlSession.selectList("USER.query", param);
	}

	@Override
	public List<User> search(String query) {
		List<User> users = sqlSession.selectList("USER.search", query);
		for (User user : users) {
			String userId = user.getId();
			if (user != null) {
				List<Kid> kids = kidDao.listByUserId(userId);
				user.setKids(kids);
			}
		}
		return users;
	}

	@Override
	public void remove(String userId) {
		sqlSession.selectOne("USER.remove", userId);
	}

	@Override
	public void addCredit(String userId, CreditRule creditRule) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("credit", creditRule.getCredit());
		map.put("goldenMedal", creditRule.getGoldenMedal());
		map.put("silverMedal", creditRule.getSilverMedal());
		map.put("bronzeMedal", creditRule.getBronzeMedal());
		sqlSession.update("USER.addCredit", map);
	}

	@Override
	public void addCredit(String userId, CreditRecord creditRecord) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("credit", creditRecord.getCredit());
		map.put("goldenMedal", creditRecord.getGoldenMedal());
		map.put("silverMedal", creditRecord.getSilverMedal());
		map.put("bronzeMedal", creditRecord.getBronzeMedal());
		sqlSession.update("USER.addCredit", map);
	}

	@Override
	public void withdrawCredit(String userId, CreditRecord creditRecord) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		map.put("credit", creditRecord.getCredit());
		map.put("goldenMedal", creditRecord.getGoldenMedal());
		map.put("silverMedal", creditRecord.getSilverMedal());
		map.put("bronzeMedal", creditRecord.getBronzeMedal());
		sqlSession.update("USER.withdrawCredit", map);
	}

	@Override
	public List<Grade> getGrades(String userId) {
		List<Grade> grades = sqlSession.selectList("USER.getGrade", userId);
		return grades;
	}

	@Override
	public LoginUser getLoginUserByMobileNumber(String mobileNumber) {
		LoginUser user = sqlSession.selectOne("USER.getLoginUserByMobileNumber", mobileNumber);
		if (user != null) {
			List<Kid> kids = kidDao.listByUserId(user.getId());
			user.setKids(kids);
		}
		return user;
	}

	@Override
	public List<BaseGrade> getBaseGradesByEventType(String eventTypeId, int  start, int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", eventTypeId);
		params.put("start", start);
		params.put("limit", limit);
		
		List<BaseGrade> grades = sqlSession.selectList("USER.getBaseGrades", params);
		return grades;
	}

	@Override
	public LoginUser updatePassword(LoginUser existedUser) {
		sqlSession.update("USER.updatePassword", existedUser);
		return getLoginUserByMobileNumber(existedUser.getMobileNum());
	}

	@Override
	public UserStat getUserStat(String userId) {
		return sqlSession.selectOne("USER.getUserStat", userId);
	}
}
