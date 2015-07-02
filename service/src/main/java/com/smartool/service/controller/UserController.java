package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.User;
import com.smartool.service.dao.UserDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class UserController {
	private static final String FAKE_CODE = "8888";
	private static final String FAKE_USER_ID = "8888";
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId, @CookieValue("mac-test") String fooCookie) {
		System.out.println(fooCookie);
		return userDao.getUserById(userId);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userDao.listAllUser();
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
	public ResponseEntity<User> register(@RequestParam(value = "code", required = false) String securityCode,
			@RequestParam(value = "code", required = false) String mobileNumber, @RequestBody User user) {
		// return token
		if (!userDao.isValidSecurityCode(mobileNumber, securityCode)) {
			return new ResponseEntity<User>(null, null, HttpStatus.BAD_REQUEST);
		}
		User createdUser = userDao.createUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", "mac-test=" + FAKE_USER_ID);
		return new ResponseEntity<User>(createdUser, headers, HttpStatus.OK);
	}

	/**
	 * 1. Get the SMS verify code
	 * 2. Verify captcha
	 */
	@RequestMapping(value = "/users/code", method = RequestMethod.POST)
	public String createCode(@RequestParam(value = "mobileNum", required = false) String mobileNum) {
		return userDao.generateSecurityCode(mobileNum);
	}

	/**
	 * Return the qrcode with url+token
	 * 
	 
	@RequestMapping(value = "qrcode", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getQRCode(@RequestParam(value = "userId", required = true) String userId) {
		
	}*/

}