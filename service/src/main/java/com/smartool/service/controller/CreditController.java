package com.smartool.service.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.CreditRuleType;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class CreditController extends BaseController {
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrules", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<CreditRuleType> listAll() {
		return null;
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrule/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<String> listAllRuleTypes() {
		return null;
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/creditrule/types/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<CreditRuleType> listAllRuleTypes(@PathVariable("creditRuleType") String creditRuleType) {
		return null;
	}
}
