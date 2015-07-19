package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.EventType;

public class EventTypeDaoImpl implements EventTypeDao {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<EventType> list() {
		return sqlSession.selectList("EVENT_TYPE.list");
	}

	@Override
	public EventType get(String id) {
		return getInternal(id);
	}

	@Override
	public EventType update(EventType eventType) {
		sqlSession.insert("EVENT_TYPE.update", eventType);
		return getInternal(eventType.getId());
	}

	@Override
	public void delete(String id) {
		sqlSession.selectOne("EVENT_TYPE.remove", id);
	}

	@Override
	public EventType create(EventType eventType) {
		sqlSession.insert("EVENT_TYPE.create", eventType);
		return  getInternal(eventType.getId());
	}
	
	private EventType getInternal(String id) {
		return sqlSession.selectOne("EVENT_TYPE.get", id);
	}

}
