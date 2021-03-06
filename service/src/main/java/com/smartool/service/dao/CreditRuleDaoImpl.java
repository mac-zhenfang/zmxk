package com.smartool.service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;

public class CreditRuleDaoImpl implements CreditRuleDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public CreditRule createCreditRule(CreditRule creditRule) {
		sqlSession.insert("CREDIT_RULE.create", creditRule);
		return getCreditRuleByIdInternal(creditRule.getId());
	}

	@Override
	public CreditRule updateCreditRule(CreditRule creditRule) {
		sqlSession.update("CREDIT_RULE.update", creditRule);
		return getCreditRuleByIdInternal(creditRule.getId());
	}

	private CreditRule getCreditRuleByIdInternal(String creditRuleId) {
		return sqlSession.selectOne("CREDIT_RULE.getById", creditRuleId);
	}

	@Override
	public CreditRule getCreditRuleById(String creditRuleId) {
		return getCreditRuleByIdInternal(creditRuleId);
	}

	@Override
	public CreditRule getCreditRuleByName(String creditRuleName) {
		return sqlSession.selectOne("CREDIT_RULE.getByName", creditRuleName);
	}

	@Override
	public void removeCreditRule(String creditRuleId) {
		sqlSession.delete("CREDIT_RULE.remove", creditRuleId);
	}

	@Override
	public List<CreditRule> listAllCreditRules() {
		return sqlSession.selectList("CREDIT_RULE.listAll");
	}

	@Override
	public EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule) {
		sqlSession.insert("EVENT_CREDIT_RULE.create", eventCreditRule);
		return getEventCreditRuleByIdInternal(eventCreditRule.getId());
	}

	@Override
	public EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule) {
		sqlSession.update("EVENT_CREDIT_RULE.update", eventCreditRule);
		return getEventCreditRuleByIdInternal(eventCreditRule.getId());
	}

	@Override
	public EventCreditRule getEventCreditRuleById(String eventCreditRuleId) {
		return getEventCreditRuleByIdInternal(eventCreditRuleId);
	}

	private EventCreditRule getEventCreditRuleByIdInternal(String eventCreditRuleId) {
		return sqlSession.selectOne("EVENT_CREDIT_RULE.getById", eventCreditRuleId);
	}

	@Override
	public void removeEventCreditRule(String eventCreditRuleId) {
		sqlSession.delete("EVENT_CREDIT_RULE.remove", eventCreditRuleId);
	}

	@Override
	public List<EventCreditRule> listAllEventCreditRules() {
		return sqlSession.selectList("EVENT_CREDIT_RULE.listAll");
	}

	/*@Override
	public List<EventCreditRule> listRankingEventCreditRules(String eventTypeId, Integer stage, String name) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (eventTypeId != null) {
			param.put("eventTypeId", eventTypeId);
		}
		if (name != null) {
			param.put("name", name);
		}
		if (stage != null) {
			param.put("stage", stage.intValue());
		}
		return sqlSession.selectList("EVENT_CREDIT_RULE.searchRankingRules", param);
	}*/

	@Override
	public List<EventCreditRule> listNonrankingEventCreditRules(String eventTypeId, Integer stage, String name) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (eventTypeId != null) {
			param.put("eventTypeId", eventTypeId);
		}
		if (name != null) {
			param.put("name", name);
		}
		if (stage != null) {
			param.put("stage", stage);
		}
		return sqlSession.selectList("EVENT_CREDIT_RULE.searchNonrankingRules", param);
	}

	@Override
	public List<EventCreditRule> getEventCreditRuleByName(String name) {
		return sqlSession.selectList("EVENT_CREDIT_RULE.getByName", name);
	}

	@Override
	public List<EventCreditRule> listRankingEventCreditRules(String eventTypeId, Integer stage, String name) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (eventTypeId != null) {
			param.put("eventTypeId", eventTypeId);
		}
		if (name != null) {
			param.put("name", name);
		}
		if (stage != null) {
			param.put("stage", stage.intValue());
		}
		return sqlSession.selectList("EVENT_CREDIT_RULE.searchRankingRules", param);
	}
}
