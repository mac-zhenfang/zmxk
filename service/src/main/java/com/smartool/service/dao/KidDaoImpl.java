package com.smartool.service.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Kid;

public class KidDaoImpl implements KidDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public Kid create(Kid kid) {
		sqlSession.insert("KID.create", kid);
		return sqlSession.selectOne("KID.getById", kid.getId());
	}

	@Override
	public Kid update(Kid kid) {
		sqlSession.insert("KID.update", kid);
		return sqlSession.selectOne("KID.getById", kid.getId());
	}

	@Override
	public Kid get(String id) {
		return sqlSession.selectOne("KID.getById", id);
	}

	@Override
	public List<Kid> listAll() {
		return sqlSession.selectList("KID.listAll");
	}

	@Override
	public List<Kid> listByUserId(String userId) {
		return sqlSession.selectList("KID.listByUserId", userId);
	}

	@Override
	public void remove(String id) {
		sqlSession.delete("KID.remove", id);
	}

	@Override
	public void removeByUserId(String userId) {
		sqlSession.delete("KID.listByUserId", userId);
	}


	@Override
	public Kid updateAvatarUrl(String kidId, String avatarUrl) {
		Kid kid = get(kidId);
		kid.setAvatarUrl(avatarUrl);
		sqlSession.update("KID.setAvatar", kid);
		return kid;
	}

	@Override
	public List<String> getDistinctSchoolName(int schoolType) {
		return sqlSession.selectList("KID.getDistinctSchoolName", schoolType);
	}

	@Override
	public void setFirstAttendEventTime(String kidId, Date eventTime) {
		// TODO Auto-generated method stub firstTimeAttend
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kidId", kidId);
		params.put("firstTimeAttendEvent", eventTime);
	}
}
