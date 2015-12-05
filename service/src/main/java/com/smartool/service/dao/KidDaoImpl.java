package com.smartool.service.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.smartool.common.dto.Kid;

public class KidDaoImpl implements KidDao {
	@Autowired
	private SqlSession sqlSession;
	

	static ObjectMapper om = new ObjectMapper();

	@Override
	public Kid create(Kid kid) {
		int i = 0;
		Kid retKid = null;
		int retryCount = 3;
		Exception throwE = null;
		while(i++<3)
		try {
			int kidNum = sqlSession.selectOne("getKidsCount");
			kid.setKidNum(kidNum);
			sqlSession.insert("KID.create", kid);
			retKid = sqlSession.selectOne("KID.getById", kid.getId());
			break;
		}catch(Exception e) {
			throwE = e;
		}
		if(i == retryCount && throwE!=null) {
			throw new RuntimeException(throwE);
		}
		
		return retKid;
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
		sqlSession.update("KID.firstTimeAttend", params);
	}

	@Override
	public void setTeams(String kidId, String teamId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> teamIdList = getTeams(kidId);
		boolean contains = false;
		for (String id : teamIdList) {
			if (id.equals(teamId)) {
				contains = true;
				break;
			}
		}
		if (!contains) {

			try {
				teamIdList.add(teamId);
				params.put("id", kidId);
				String teamString = om.writeValueAsString(teamIdList);
				params.put("teams", teamString);
				sqlSession.update("setTeams", params);
			} catch (JsonProcessingException e) {

			}

		}

	}

	@Override
	public List<String> getTeams(String id) {
		String teamString = sqlSession.selectOne("KID.getTeams", id);
		List<String> teamIdList = new ArrayList<>();
		if (!Strings.isNullOrEmpty(teamString)) {
			try {
				teamIdList = om.readValue(teamString, new TypeReference<List<String>>() {
				});
			} catch (IOException e) {

			}
		}
		return teamIdList;
	}

	@Override
	public void leaveTeams(String kidId, String teamId) {
				Map<String, Object> params = new HashMap<String, Object>();
				List<String> teamIdList = getTeams(kidId);
				List<String> setTeamIdList = new ArrayList<>();
				boolean contains = false;
				for (String id : teamIdList) {
					if (!id.equals(teamId)) {
						setTeamIdList.add(id);
					} else {
						contains = true;
					}
				}
				if (contains) {
					try {
						params.put("id", kidId);
						String teamString = om.writeValueAsString(setTeamIdList);
						params.put("teams", teamString);
						sqlSession.update("setTeams", params);
					} catch (JsonProcessingException e) {

					}

				}
		
	}

	@Override
	public Kid updateCoverUrl(String kidId, String coverUrl) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", kidId);
		params.put("coverUrl", coverUrl);
		sqlSession.update("KID.setCover", params);
		return get(kidId);
	}
}
