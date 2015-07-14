package com.smartool.service.dao;

import java.util.List;

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

	public CreditRule getCreditRuleByIdInternal(String creditRuleId) {
		return sqlSession.selectOne("CREDIT_RULE.getById", creditRuleId);
	}

	@Override
	public CreditRule getCreditRuleById(String creditRuleId) {
		return getCreditRuleByIdInternal(creditRuleId);
	}

	@Override
	public void removeCreditRule(String creditRuleId) {
		sqlSession.delete("CREDIT_RULE.remove", creditRuleId);
	}

	@Override
	public List<CreditRule> listAllCreditRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCreditRule createEventCreditRule(EventCreditRule eventCreditRule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCreditRule updateEventCreditRule(EventCreditRule eventCreditRule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCreditRule getEventCreditRuleById(String eventCreditRuleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeEventCreditRule(String eventCreditRuleId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<EventCreditRule> listAllEventCreditRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listEventCreditName(String eventId, String seriesId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventCreditRule> listEventCreditRules(String eventId, String seriesId, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventCreditRule getEventCreditRule(String eventId, String seriesId, String name, Integer rank) {
		// TODO Auto-generated method stub
		return null;
	}

}
