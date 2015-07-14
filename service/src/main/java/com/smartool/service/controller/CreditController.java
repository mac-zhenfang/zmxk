package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.CreditRule;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.CreditRuleDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class CreditController extends BaseController {
	@Autowired
	private CreditRuleDao creditRuleDao;

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules", method = RequestMethod.GET)
	@ResponseBody
	List<CreditRule> listAll() {
		return creditRuleDao.listAllCreditRules();
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule create(@RequestBody CreditRule creditRule) {
		creditRule.setId(CommonUtils.getRandomUUID());
		return creditRuleDao.createCreditRule(creditRule);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule get(@PathVariable("creditRuleId") String creditRuleId) {
		return creditRuleDao.getCreditRuleById(creditRuleId);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule update(@PathVariable("creditRuleId") String creditRuleId, @RequestBody CreditRule creditRule) {
		creditRule.setId(creditRuleId);
		return creditRuleDao.updateCreditRule(creditRule);
	}
}
