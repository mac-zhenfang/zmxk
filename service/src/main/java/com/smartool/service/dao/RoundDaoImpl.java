package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Round;

public class RoundDaoImpl implements RoundDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<Round> listAll() {
		return sqlSession.selectList("ROUND.listAll");
	}

	@Override
	public List<Round> listAll(Integer level) {
		return sqlSession.selectList("ROUND.listByLevel", level);
	}

	@Override
	public Round get(String roundId) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("ROUND.get", roundId);
	}

	@Override
	public List<Round> listGroupByLevel() {
		return sqlSession.selectList("ROUND.groupLevel");
	}

}
