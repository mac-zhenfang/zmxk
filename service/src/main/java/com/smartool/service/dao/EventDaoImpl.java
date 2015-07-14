package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Event;

public class EventDaoImpl implements EventDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<Event> listAllEvent() {
		return sqlSession.selectList("EVENT.listAll");
	}

	@Override
	public Event createEvent(Event event) {
		sqlSession.selectOne("EVENT.create", event);
		return getEventInternal(event.getId());
	}

	@Override
	public Event updateEvent(Event event) {
		sqlSession.selectOne("EVENT.update", event);
		return getEventInternal(event.getId());
	}

	private Event getEventInternal(String eventId) {
		return sqlSession.selectOne("EVENT.getById", eventId);
	}

	@Override
	public Event getEvent(String eventId) {
		return getEventInternal(eventId);
	}

	@Override
	public Event getFullEvent(String eventId) {
		return sqlSession.selectOne("EVENT.getFullById", eventId);
	}

	@Override
	public List<Event> listAllEvent(int status) {
		return sqlSession.selectList("EVENT.listAllStatus", status);
	}

	@Override
	public List<Event> listFullEvent() {
		return sqlSession.selectList("EVENT.listFullAll");
	}
}
