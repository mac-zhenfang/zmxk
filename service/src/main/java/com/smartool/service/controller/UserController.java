package com.smartool.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class UserController {
	
	@RequestMapping(value="users/{userId}", method=RequestMethod.GET)
	public String test (@PathVariable String userId) {
		return userId;
	}
}
