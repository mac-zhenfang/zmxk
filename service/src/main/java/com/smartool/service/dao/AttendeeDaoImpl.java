package com.smartool.service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Attendee;

public class AttendeeDaoImpl implements AttendeeDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public Attendee create(Attendee attendee) {
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

	@Override
	public Attendee updateNotifyTimes(Attendee attendee) {
		sqlSession.update("ATTENDEE.updateNotifyTimes", attendee);
		return getAttendeeInternal(attendee.getId());
	}

	@Override
	public Attendee enroll(Attendee attendee) {
		sqlSession.selectOne("ATTENDEE.enroll", attendee);
		return getAttendeeInternal(attendee.getId());
	}

	@Override
	public List<Attendee> getAllPendingAttendees(String eventId) {
		return sqlSession.selectList("ATTENDEE.getPendingAttendees", eventId);
	}

	@Override
	public Attendee complete(Attendee attendee) {
		sqlSession.selectOne("ATTENDEE.complete", attendee);
		return getAttendeeInternal(attendee.getId());
	}

	@Override
	public void removeUnused(String eventId) {
		sqlSession.delete("ATTENDEE.removeUnused", eventId);
	}

	@Override
	public Attendee update(Attendee attendee) {
		sqlSession.selectOne("ATTENDEE.update", attendee);
		return getAttendeeInternal(attendee.getId());
	}

	@Override
	public Map<String, Attendee> getAttendeesFromEvent(String eventId, String roundId) {
		Map<String, String> param = new HashMap<>();
		param.put("eventId", eventId);
		param.put("roundId", roundId);

		return sqlSession.selectMap("ATTENDEE.getByEventRoundId", param, "id");
	}

	@Override
	public Attendee updateNextRound(Attendee attendee) {
		sqlSession.selectOne("ATTENDEE.updateNextRound", attendee);
		return getAttendeeInternal(attendee.getId());
	}
}
