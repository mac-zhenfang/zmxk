package com.smartool.service.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.Event;
import com.smartool.service.service.EventStartNotificationJob;

public class EventDaoImpl implements EventDao {
	
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	Scheduler scheduler;
	
	public void iocInit() throws Exception {
		if (scheduler.getTrigger(TriggerKey.triggerKey("EventStartNotificationJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventStartNotificationJob.class)
					.withIdentity(JobKey.jobKey("EventStartNotificationJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("EventStartNotificationJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}
	}

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
}
