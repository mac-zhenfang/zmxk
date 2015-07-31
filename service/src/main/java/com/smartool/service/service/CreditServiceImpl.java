package com.smartool.service.service;

import java.util.List;
import java.util.Objects;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.CreditRuleType;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.common.dto.EventStages;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.dao.CreditRecordDao;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.SerieDao;
import com.smartool.service.dao.UserDao;

public class CreditServiceImpl implements CreditService {
	@Autowired
	private CreditRuleDao creditRuleDao;
	@Autowired
	private CreditRecordDao creditRecordDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private SerieDao serieDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private Scheduler scheduler;

	public void iocInit() throws SchedulerException {
		if (scheduler.getTrigger(TriggerKey.triggerKey("CreditGeneratorTrigger")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(CreditGeneratingJob.class)
					.withIdentity(JobKey.jobKey("CreditGeneratorJob")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("CreditGeneratorTrigger"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}
	}

	@Override
	public String getCreditRecordDisplayName(Attendee attendee, CreditRule creditRule) {
		if (creditRule == null) {
			return null;
		}
		if (creditRule instanceof EventCreditRule) {
			EventCreditRule eventCreditRule = (EventCreditRule) creditRule;
			StringBuilder sb = new StringBuilder();
			if (eventCreditRule.getSeriesId() != null) {
				String serieName = serieDao.get(eventCreditRule.getSeriesId()).getName();
				sb.append(serieName).append(" - ");
			}
			String eventName = eventDao.getEvent(attendee.getEventId()).getName();
			sb.append(eventName);
			if (eventCreditRule.getStage() != null) {
				sb.append(" - ").append(EventStages.getDisplayName(eventCreditRule.getStage()));
			}
			if (eventCreditRule.getUpperRank() != null || eventCreditRule.getLowerRank() != null) {
				sb.append(" - ").append(attendee.getRank());
			}
			return sb.toString();
		}

		return creditRule.getName();
	}

	@Override
	public List<CreditRule> listAllCreditRules() {
		return creditRuleDao.listAllCreditRules();
	}

	@Override
	public CreditRule createCreditRule(CreditRule creditRule) {
		creditRuleValidToCreate(creditRule);
		creditRule.setId(CommonUtils.getRandomUUID());
		return creditRuleDao.createCreditRule(creditRule);
	}

	private boolean creditRuleValidToCreate(CreditRule creditRule) {
		if (CommonUtils.isEmptyString(creditRule.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.EMPTY_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		if (creditRuleDao.getCreditRuleByName(creditRule.getName()) != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.DUPLICATED_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		return true;
	}

	private boolean creditRuleValidToUpdate(CreditRule creditRule) {
		if (CommonUtils.isEmptyString(creditRule.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.EMPTY_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		CreditRule existedCreditRule = creditRuleDao.getCreditRuleByName(creditRule.getName());
		if (existedCreditRule != null && !existedCreditRule.getId().equals(creditRule.getId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.DUPLICATED_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		return true;
	}

	@Override
	public CreditRule getCreditRuleById(String creditRuleId) {
		return creditRuleDao.getCreditRuleById(creditRuleId);
	}

	@Override
	public CreditRule updateCreditRule(CreditRule creditRule) {
		creditRuleValidToUpdate(creditRule);
		return creditRuleDao.updateCreditRule(creditRule);
	}

	@Override
	public void removeCreditRule(String creditRuleId) {
		creditRuleDao.removeCreditRule(creditRuleId);
	}

	@Override
	public List<EventCreditRule> listAllEventCreditRules() {
		return creditRuleDao.listAllEventCreditRules();
	}

	@Override
	public EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule) {
		eventCreditRuleValidToCreate(eventCreditRule);
		eventCreditRule.setId(CommonUtils.getRandomUUID());
		return creditRuleDao.createEventCreditRule(eventCreditRule);
	}

	@Override
	public EventCreditRule getEventCreditRuleById(String eventCreditRuleId) {
		return creditRuleDao.getEventCreditRuleById(eventCreditRuleId);
	}

	private boolean eventCreditRuleValidToCreate(EventCreditRule eventCreditRule) {
		if (CommonUtils.isEmptyString(eventCreditRule.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.EMPTY_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		List<EventCreditRule> existedCreditRules = creditRuleDao.getEventCreditRuleByName(eventCreditRule.getName());
		for (EventCreditRule existedCreditRule : existedCreditRules) {
			if (existedCreditRule != null && existedCreditRule.getEventTypeId().equals(eventCreditRule.getEventTypeId())
					&& Objects.equals(existedCreditRule.getSeriesId(), eventCreditRule.getSeriesId())
					&& Objects.equals(existedCreditRule.getUpperRank(), eventCreditRule.getUpperRank())
					&& Objects.equals(existedCreditRule.getLowerRank(), eventCreditRule.getLowerRank())
					&& Objects.equals(existedCreditRule.getStage(), eventCreditRule.getStage())) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
						ErrorMessages.DUPLICATED_CREDIT_RULE_NAME_ERROR_MESSAGE);
			}
		}
		return true;
	}

	private boolean eventCreditRuleValidToUpdate(EventCreditRule eventCreditRule) {
		if (CommonUtils.isEmptyString(eventCreditRule.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.EMPTY_CREDIT_RULE_NAME_ERROR_MESSAGE);
		}
		List<EventCreditRule> existedCreditRules = creditRuleDao.getEventCreditRuleByName(eventCreditRule.getName());
		for (EventCreditRule existedCreditRule : existedCreditRules) {
			if (existedCreditRule != null && existedCreditRule.getEventTypeId().equals(eventCreditRule.getEventTypeId())
					&& Objects.equals(existedCreditRule.getSeriesId(), eventCreditRule.getSeriesId())
					&& Objects.equals(existedCreditRule.getUpperRank(), eventCreditRule.getUpperRank())
					&& Objects.equals(existedCreditRule.getLowerRank(), eventCreditRule.getLowerRank())
					&& Objects.equals(existedCreditRule.getStage(), eventCreditRule.getStage())
					&& !existedCreditRule.getId().equals(eventCreditRule.getId())) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
						ErrorMessages.DUPLICATED_CREDIT_RULE_NAME_ERROR_MESSAGE);
			}
		}
		return true;
	}

	@Override
	public EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule) {
		eventCreditRuleValidToUpdate(eventCreditRule);
		return creditRuleDao.updateEventCreditRule(eventCreditRule);
	}

	@Override
	public void removeEventCreditRule(String eventCreditRuleId) {
		creditRuleDao.removeEventCreditRule(eventCreditRuleId);
	}

	@Override
	public List<EventCreditRule> searchRankingEventCreditRules(String eventTypeId, Integer stage, String seriesId,
			String name) {
		return creditRuleDao.listRankingEventCreditRules(eventTypeId, stage, seriesId, name);
	}

	@Override
	public List<EventCreditRule> searchNonrankingEventCreditRules(String eventTypeId, Integer stage, String seriesId,
			String name) {
		return creditRuleDao.listNonrankingEventCreditRules(eventTypeId, stage, seriesId, name);
	}

	@Override
	public CreditRecord applyCreditRull(Attendee attendee, CreditRule creditRule) {
		userDao.addCredit(attendee.getUserId(), creditRule);
		CreditRecord creditRecord = new CreditRecord();
		creditRecord.setId(CommonUtils.getRandomUUID());
		creditRecord.setAttendeeId(attendee.getId());
		creditRecord.setEventId(attendee.getEventId());
		creditRecord.setUserId(attendee.getUserId());
		creditRecord.setCreditRuleId(creditRule.getId());
		creditRecord.setCredit(creditRule.getCredit());
		creditRecord.setScore(attendee.getScore());
		creditRecord.setDisplayName(getCreditRecordDisplayName(attendee, creditRule));
		if (creditRule instanceof EventCreditRule) {
			creditRecord.setCreditRuleType(CreditRuleType.EVENT);
			EventCreditRule eventCreditRule = (EventCreditRule) creditRule;
			if (eventCreditRule.getUpperRank() != null || eventCreditRule.getLowerRank() != null) {
				creditRecord.setRank(attendee.getRank());
			}
		} else {
			creditRecord.setCreditRuleType(CreditRuleType.NORMAL);
		}
		creditRecord.setGoldenMedal(creditRecord.getGoldenMedal());
		creditRecord.setSilverMedal(creditRecord.getSilverMedal());
		creditRecord.setBronzeMedal(creditRecord.getBronzeMedal());
		CreditRecord createdCreditRecord = creditRecordDao.createCreditRecord(creditRecord);
		return createdCreditRecord;
	}

	@Override
	public List<CreditRecord> listCreditRecords(String userId, Long start, Long end) {
		return creditRecordDao.listCreditRecords(userId, start, end);
	}

	@Override
	public List<CreditRecord> listCreditRecordsByMobileNumber(String mobileNum, Long start, Long end) {
		return creditRecordDao.listCreditRecordsByMobileNumber(mobileNum, start, end);
	}
}
