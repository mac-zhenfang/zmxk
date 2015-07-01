package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.User;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class UserController {
	private static final String FAKE_CODE = "8888";
	private static final String FAKE_USER_ID = "8888";
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId, @CookieValue("mac-test") String fooCookie) {
		System.out.println(fooCookie);
		User user = new User();
		return user;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		List<User> userList = new ArrayList<User>();
		return userList;
	}

	/**
	 * 1. verify sms code
	 * 2. ask wechat for user profile (Oauth)
	 * 3. create user
	 * 4. return userId (response body) and token (response cookie)
	 * Verify the SMS code and register the user, return token
	 * 
	 * @return userId String
	 */
	@RequestMapping(value = "/users/register", method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestParam(value = "code", required = false) String code) {
		// return token
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie","mac-test="+FAKE_USER_ID);
		return new ResponseEntity<String>(FAKE_USER_ID,headers,HttpStatus.OK);    
	}

	/**
	 * 1. Get the SMS verify code
	 * 2. Verify captcha
	 */
	@RequestMapping(value = "/users/code", method = RequestMethod.POST)
	public String createCode(@RequestParam(value = "mobileNum", required = false) String mobileNum) {
		return FAKE_CODE;
	}

	/**
	 * Return the qrcode with url+token
	 * 
	 
	@RequestMapping(value = "qrcode", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getQRCode(@RequestParam(value = "userId", required = true) String userId) {
		
	}*/

}