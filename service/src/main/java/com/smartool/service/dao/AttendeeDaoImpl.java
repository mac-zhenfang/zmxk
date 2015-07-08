package com.smartool.service.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Attendee;

public class AttendeeDaoImpl implements AttendeeDao {
	

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public Attendee enroll(Attendee attendee) {
		sqlSession.selectOne("ATTENDEE.create", attendee);
		return getAttendeeInternal(attendee.getId());
	}
	
	public Attendee getAttendee(String attendeeId) {
		return getAttendeeInternal(attendeeId);
	}
	
	private Attendee getAttendeeInternal(String attendeeId) {
		return sqlSession.selectOne("ATTENDEE.getById", attendeeId);
	}

	@Override
	public List<Attendee> getAttendeeFromEvent(String eventId) {
		return sqlSession.selectList("ATTENDEE.getByEventId", eventId);
	}
}
