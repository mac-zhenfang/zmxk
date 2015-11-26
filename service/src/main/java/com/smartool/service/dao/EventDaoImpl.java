package com.smartool.service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public void delete(String eventId) {
		sqlSession.selectOne("EVENT.remove", eventId);
	}

	@Override
	public List<Event> listAllEvent(String siteId) {
		return sqlSession.selectList("EVENT.listAllBySiteId", siteId);
	}

	@Override
	public List<Event> search(Map<String, Object> param) {
		return sqlSession.selectList("EVENT.search", param);
	}

	@Override
	public void backup(Event event) {
		sqlSession.insert("EVENT.createHist", event);
	}

	@Override
	public List<Event> listAllEvent(long interval) {
		return sqlSession.selectList("EVENT.listAllEventInterval", interval);
	}

	@Override
	public void removeFromHis(long inteval) {
		sqlSession.delete("EVENT.removeFromHis", inteval);
	}

	@Override
	public List<Event> ListAllEvent(int status, long start, long end) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("leftMin", start);
		params.put("rightMax", end);
		return sqlSession.selectList("EVENT.listAllStatusTime", params);
	}
}
