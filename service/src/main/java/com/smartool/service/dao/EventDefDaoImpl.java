package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.EventDef;

public class EventDefDaoImpl implements EventDefDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<EventDef> listEventDefBySerieId(String serieId) {
		return sqlSession.selectList("EVENT_DEF.listBySerieId", serieId);
	}

}
