package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.User;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.service.CreditService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class CreditRecordsController extends BaseController {
	@Autowired
	private CreditService creditService;

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventcreditrecords/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<CreditRecord> searchCreditRecord(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "start", required = false) Long start,
			@RequestParam(value = "end", required = false) Long end) {
		return creditService.listCreditRecords(userId, start, end);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/eventcreditrecords/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<CreditRecord> searchMyCreditRecord(@RequestParam(value = "start", required = false) Long start,
			@RequestParam(value = "end", required = false) Long end) {
		User sessionUser = UserSessionManager.getSessionUser();
		return creditService.listCreditRecords(sessionUser.getId(), start, end);
	}
}
