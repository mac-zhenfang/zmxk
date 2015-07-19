package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Serie;

public class SerieDaoImpl implements SerieDao {
	
	@Autowired
	private SqlSession sqlSession;
	@Override
	public List<Serie> list(String eventTypeId) {
		// TODO Auto-generated method stub
		return sqlSession.selectList("SERIE.listByEventType", eventTypeId);
	}

	@Override
	public List<Serie> list() {
		return sqlSession.selectList("SERIE.listAll");
	}

	@Override
	public Serie get(String id) {
		return getInternal(id);
	}

	@Override
	public Serie update(Serie serie) {
		sqlSession.selectOne("SERIE.update", serie);
		return getInternal(serie.getId());
	}

	@Override
	public void delete(String serieId) {
		sqlSession.selectOne("SERIE.remove", serieId);
	}

	@Override
	public Serie create(Serie serie) {
		sqlSession.insert("SERIE.create", serie);
		return  getInternal(serie.getId());
	}
	
	private Serie getInternal(String id) {
		return sqlSession.selectOne("SERIE.get", id);
	}

	@Override
	public void batchDeleteByEventType(String eventTypeId) {
		sqlSession.selectOne("SERIE.removeByEventType", eventTypeId);
	}

}
