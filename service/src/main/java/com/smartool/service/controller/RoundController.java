package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Round;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.RoundDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class RoundController {

	@Autowired
	RoundDao roundDao;
	
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/rounds", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Round> getUsers(@RequestParam(value = "level", required = false) Integer level) {
		List<Round> rounds = null;
		if (null == level) {
			rounds = roundDao.listAll();
		} else {
			rounds = roundDao.listAll(level);
		}
		return rounds;
	}
}
