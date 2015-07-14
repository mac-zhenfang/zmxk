package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;

public interface CreditRuleDao {
	CreditRule createCreditRule(CreditRule creditRule);

	CreditRule updateCreditRule(CreditRule creditRule);

	CreditRule getCreditRuleById(String creditRuleId);

	void removeCreditRule(String creditRuleId);

	List<CreditRule> listAllCreditRules();

	EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule);

	EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule);

	EventCreditRule getEventCreditRuleById(String eventCreditRuleId);

	void removeEventCreditRule(EventCreditRule eventCreditRule);

	List<EventCreditRule> listAllEventCreditRules();

	List<String> listEventCreditName(String eventId, String seriesId);

	List<EventCreditRule> listEventCreditRules(String eventId, String seriesId, String name);

	EventCreditRule getEventCreditRule(String eventId, String seriesId, String name, Integer rank);
}
