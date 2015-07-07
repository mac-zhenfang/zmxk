package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Attendee;
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
		return sqlSession.selectOne("EVENT.createEvent");
	}

	@Override
	public Event updateEvent(Event event) {
		return sqlSession.selectOne("EVENT.updateEvent");
	}

	@Override
	public Attendee enroll(Attendee attendee) {
		return sqlSession.selectOne("ATTENDEE.create");
	}

}
