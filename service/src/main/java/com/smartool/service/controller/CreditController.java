package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.service.CreditService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class CreditController extends BaseController {@Autowired
	private CreditService creditService;

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules", method = RequestMethod.GET)
	@ResponseBody
	List<CreditRule> listAll() {
		return creditService.listAllCreditRules();
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule create(@RequestBody CreditRule creditRule) {
		return creditService.createCreditRule(creditRule);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule get(@PathVariable("creditRuleId") String creditRuleId) {
		return creditService.getCreditRuleById(creditRuleId);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	CreditRule update(@PathVariable("creditRuleId") String creditRuleId, @RequestBody CreditRule creditRule) {
		creditRule.setId(creditRuleId);
		return creditService.updateCreditRule(creditRule);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	void remove(@PathVariable("creditRuleId") String creditRuleId) {
		creditService.removeCreditRule(creditRuleId);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules", method = RequestMethod.GET)
	@ResponseBody
	List<EventCreditRule> listAllEventCreditRules() {
		return creditService.listAllEventCreditRules();
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	EventCreditRule createEventCreditRule(@RequestBody EventCreditRule eventCreditRule) {
		return creditService.createEventCreditRule(eventCreditRule);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	EventCreditRule getEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId) {
		return creditService.getEventCreditRuleById(eventCreditRuleId);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	EventCreditRule updateEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId,
			@RequestBody EventCreditRule eventCreditRule) {
		eventCreditRule.setId(eventCreditRuleId);
		return creditService.updateEventCreditRule(eventCreditRule);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	void removeEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId) {
		creditService.removeEventCreditRule(eventCreditRuleId);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules/ranking/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<EventCreditRule> searchRankingEventCreditRules(
			@RequestParam(value = "eventTypeId", required = false) String eventTypeId,
			@RequestParam(value = "seriesId", required = false) String seriesId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "stage", required = false) Integer stage) {
		return creditService.searchRankingEventCreditRules(eventTypeId, stage, seriesId, name);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrules/nonranking/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<EventCreditRule> searchNonrankingEventCreditRules(
			@RequestParam(value = "eventTypeId", required = false) String eventTypeId,
			@RequestParam(value = "seriesId", required = false) String seriesId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "rank", required = false) Integer rank,
			@RequestParam(value = "stage", required = false) Integer stage) {
		return creditService.searchNonrankingEventCreditRules(eventTypeId, stage, seriesId, name, rank);
	}
}
