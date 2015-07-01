package com.smartool.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/smartool/api/v1/users")
public class UserController {
	
	@RequestMapping(value="{userId}", method=RequestMethod.GET)
	public String getUser (@PathVariable String userId) {
		return userId;
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	
	/**
	 * Verify the SMS code and register the user, return token
	 * */
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(@RequestParam) {
		
	}
	/**
	 * Get the SMS verify code
	 * */
	@RequestMapping(value="code", method=RequestMethod.POST)
	/**
	 * Return the qrcode with url+token
	 * 
	 * */
	@RequestMapping(value="qrcode", method=RequestMethod.GET)

	
}