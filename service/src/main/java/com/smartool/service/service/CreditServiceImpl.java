package com.smartool.service.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.SerieDao;

public class CreditServiceImpl implements CreditService {
	@Autowired
	private CreditRuleDao creditRuleDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private SerieDao serieDao;

	@Override
	public String getDisplayName(CreditRule creditRule) {
		if (creditRule == null) {
			return null;
		}
		if (creditRule instanceof EventCreditRule) {
			EventCreditRule eventCreditRule = (EventCreditRule) creditRule;
			StringBuilder sb = new StringBuilder();
			if (eventCreditRule.getSeriesId() != null) {

			}
			sb.append(creditRule.getName());
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
					&& Objects.equals(existedCreditRule.getRank(), eventCreditRule.getRank())) {
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
					&& Objects.equals(existedCreditRule.getRank(), eventCreditRule.getRank())
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
	public List<EventCreditRule> searchRankingEventCreditRules(String eventTypeId, Integer stage, String seriesId, String name) {
		return creditRuleDao.listRankingEventCreditRules(eventTypeId, stage, seriesId, name);
	}

	@Override
	public List<EventCreditRule> searchNonrankingEventCreditRules(String eventTypeId, Integer stage, String seriesId, String name,
			Integer rank) {
		return creditRuleDao.listNonrankingEventCreditRules(eventTypeId, stage, seriesId, name, rank);
	}
}
