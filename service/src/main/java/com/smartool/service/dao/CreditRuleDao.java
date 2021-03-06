package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;

public interface CreditRuleDao {
	CreditRule createCreditRule(CreditRule creditRule);

	CreditRule updateCreditRule(CreditRule creditRule);

	CreditRule getCreditRuleById(String creditRuleId);

	CreditRule getCreditRuleByName(String creditRuleName);

	void removeCreditRule(String creditRuleId);

	List<CreditRule> listAllCreditRules();

	EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule);

	EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule);

	EventCreditRule getEventCreditRuleById(String eventCreditRuleId);

	List<EventCreditRule> getEventCreditRuleByName(String name);

	void removeEventCreditRule(String eventCreditRuleId);

	List<EventCreditRule> listAllEventCreditRules();

	//List<EventCreditRule> listRankingEventCreditRules(String eventTypeId, Integer stage, String name);
	
	List<EventCreditRule> listRankingEventCreditRules(String eventTypeId, Integer stage, String name);

	List<EventCreditRule> listNonrankingEventCreditRules(String eventTypeId, Integer stage, String name);
}
