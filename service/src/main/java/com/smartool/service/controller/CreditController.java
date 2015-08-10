package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.EventCreditRule;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.service.CreditService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class CreditController extends BaseController {
	@Autowired
	private CreditService creditService;

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules", method = RequestMethod.GET)
	public List<CreditRule> listAll() {
		return creditService.listAllCreditRules();
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public CreditRule create(@RequestBody CreditRule creditRule) {
		return creditService.createCreditRule(creditRule);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CreditRule get(@PathVariable("creditRuleId") String creditRuleId) {
		return creditService.getCreditRuleById(creditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public CreditRule update(@PathVariable("creditRuleId") String creditRuleId, @RequestBody CreditRule creditRule) {
		creditRule.setId(creditRuleId);
		return creditService.updateCreditRule(creditRule);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules/{creditRuleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void remove(@PathVariable("creditRuleId") String creditRuleId) {
		creditService.removeCreditRule(creditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/creditrules/{creditRuleId}/attendee/{attendeeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void applyCreditRule(@PathVariable("creditRuleId") String creditRuleId,
			@PathVariable("attendeeId") String attendeeId) {
		creditService.applyCreditRull(attendeeId, creditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}/attendee/{attendeeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void applyEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId,
			@PathVariable("attendeeId") String attendeeId) {
		creditService.applyEventCreditRull(attendeeId, eventCreditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules", method = RequestMethod.GET)
	public List<EventCreditRule> listAllEventCreditRules() {
		return creditService.listAllEventCreditRules();
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public EventCreditRule createEventCreditRule(@RequestBody EventCreditRule eventCreditRule) {
		return creditService.createEventCreditRule(eventCreditRule);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public EventCreditRule getEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId) {
		return creditService.getEventCreditRuleById(eventCreditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public EventCreditRule updateEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId,
			@RequestBody EventCreditRule eventCreditRule) {
		eventCreditRule.setId(eventCreditRuleId);
		return creditService.updateEventCreditRule(eventCreditRule);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/{eventCreditRuleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void removeEventCreditRule(@PathVariable("eventCreditRuleId") String eventCreditRuleId) {
		creditService.removeEventCreditRule(eventCreditRuleId);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/ranking/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EventCreditRule> searchRankingEventCreditRules(
			@RequestParam(value = "eventTypeId", required = false) String eventTypeId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "stage", required = false) Integer stage) {
		return creditService.searchRankingEventCreditRules(eventTypeId, stage, name);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventcreditrules/nonranking/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EventCreditRule> searchNonrankingEventCreditRules(
			@RequestParam(value = "eventTypeId", required = false) String eventTypeId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "stage", required = false) Integer stage) {
		return creditService.searchNonrankingEventCreditRules(eventTypeId, stage, name);
	}
}
