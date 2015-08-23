package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Team;

public class TeamDaoImpl implements TeamDao {

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
		sqlSession.update("", team);
		return internalGet(team.getId());
	}
	
	private Team internalGet(String teamId) {
		return sqlSession.selectOne("TEAM.get", teamId);
	}

	@Override
	public void delete(String teamId) {
		sqlSession.delete("Team.delete", teamId);
	}

	@Override
	public List<Team> memberOf(String userId) {
		return sqlSession.selectList("TEAM.memberOf", userId);
	}

	@Override
	public List<Kid> getMembers(String teamId) {
		return sqlSession.selectList("TEAM.members", teamId);
	}
}
