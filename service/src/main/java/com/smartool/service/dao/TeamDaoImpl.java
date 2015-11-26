package com.smartool.service.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Team;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;

public class TeamDaoImpl implements TeamDao {
	static ObjectMapper om  = new ObjectMapper();
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<Team> list() {
		return sqlSession.selectList("TEAM.list");
	}

	@Override
	public Team get(String teamId) {
		return internalGet(teamId);
	}

	@Override
	public Team create(Team team) {
		sqlSession.insert("TEAM.create", team);
		return internalGet(team.getId());
	}

	@Override
	public Team update(Team team) {
		sqlSession.update("TEAM.update", team);
		return internalGet(team.getId());
	}
	
	private Team internalGet(String teamId) {
		return sqlSession.selectOne("TEAM.get", teamId);
	}

	@Override
	public void delete(String teamId) {
		sqlSession.delete("TEAM.delete", teamId);
	}

	@Override
	public Team memberOf(String kidId, String teamId) {
		String members = sqlSession.selectOne("TEAM.members", teamId);
		List<String> memberLst = new ArrayList<>();
		if(null != members) {
			try {
				memberLst = om.readValue(members, new TypeReference<List<String>>(){});
			} catch (IOException e) {
				
			}
		}
		boolean found = false;;
		for(String id : memberLst) {
			if(id.equals(kidId)){
				found = true;
				break;
			}
		}
		if(found) {
			return this.get(teamId);
		}
		return null;
	}

	@Override
	public List<String> getMembers(String teamId) {
		String members = sqlSession.selectOne("TEAM.members", teamId);
		List<String> memberLst = new ArrayList<>();
		if(null != members) {
			try {
				memberLst = om.readValue(members, new TypeReference<List<String>>(){});
			} catch (IOException e) {
				
			}
		}
		return memberLst;
	}

	@Override
	public void addMember(Kid kid, String teamId) {
		// TODO Auto-generated method stub
		String members = sqlSession.selectOne("TEAM.members", teamId);
		List<String> memberLst = new ArrayList<>();
		try {
			if(null != members) {
				memberLst = om.readValue(members, new TypeReference<List<String>>(){});
			}
			List<String> newMemberLst = new ArrayList<>();
			newMemberLst.add(kid.getId());
			for(String memberId : memberLst) {
				if(memberId.equals(kid.getId())) {
					throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_JOIN_TEAM);
				}
				newMemberLst.add(memberId);
			}
			String newMember = om.writeValueAsString(newMemberLst);
			Map<String, Object> map = new HashMap<>();
			map.put("teamId", teamId);
			map.put("members", newMember);
			sqlSession.update("TEAM.updateMember", map);
		} catch (IOException e) {
			//throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_JOIN_TEAM);
		}
		
	}

	@Override
	public void delMember(String kidId, String teamId) {
		// TODO Auto-generated method stub
		String members = sqlSession.selectOne("TEAM.members", teamId);
		List<String> memberLst = new ArrayList<>();
		try {
			if(null != members) {
				memberLst = om.readValue(members, new TypeReference<List<String>>(){});
			}
			List<String> newMemberLst = new ArrayList<>();
			for(String memberId : memberLst) {
				if(!memberId.equals(kidId)) {
					newMemberLst.add(memberId);
				}
			}
			String newMember = om.writeValueAsString(newMemberLst);
			Map<String, Object> map = new HashMap<>();
			map.put("teamId", teamId);
			map.put("members", newMember);
			sqlSession.update("TEAM.updateMember", map);
		} catch (IOException e) {
			//throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.DUPLICATE_JOIN_TEAM);
		}
	}
	
	
}
