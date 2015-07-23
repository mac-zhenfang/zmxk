package com.smartool.service.service;

import java.util.List;

import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;

public interface CreditService {
	List<CreditRule> listAllCreditRules();

	CreditRule createCreditRule(CreditRule creditRule);

	CreditRule getCreditRuleById(String creditRuleId);

	CreditRule updateCreditRule(CreditRule creditRule);

	void removeCreditRule(String creditRuleId);

	List<EventCreditRule> listAllEventCreditRules();

	EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule);

	EventCreditRule getEventCreditRuleById(String eventCreditRuleId);

	EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule);

	void removeEventCreditRule(String eventCreditRuleId);

	List<EventCreditRule> searchRankingEventCreditRules(String eventTypeId, Integer stage, String seriesId,
			String name);

	List<EventCreditRule> searchNonrankingEventCreditRules(String eventTypeId, Integer stage, String seriesId,
			String name);

	CreditRecord applyCreditRull(Attendee attendee, CreditRule creditRule, String operatorUserId);

	List<CreditRecord> listCreditRecords(String userId, Long start, Long end);

	String getCreditRecordDisplayName(Attendee attendee, CreditRule creditRule);
}
