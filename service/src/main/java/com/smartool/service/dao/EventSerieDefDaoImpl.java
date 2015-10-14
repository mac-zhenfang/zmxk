package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.EventSerieDef;

public class EventSerieDefDaoImpl implements EventSerieDefDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<EventSerieDef> listAll() {
		return sqlSession.selectList("EVENT_SERIE_DEF.listAll");
	}
}
