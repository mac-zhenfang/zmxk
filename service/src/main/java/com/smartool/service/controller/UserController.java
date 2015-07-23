package com.smartool.service.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;
import com.smartool.service.Constants;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.service.UserService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	private HttpServletResponse httpServletResponse;

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/users/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> search(@RequestParam(value = "query", required = false) String query) {
		return userService.search(query);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userService.listAllUser();
	}

	@RequestMapping(value = Constants.USER_LOGIN_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public User login(@RequestBody User user) {
		User existUser = userService.login(user);
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, existUser);
		return existUser;
	}

	/**
	 * 1. verify sms code 2. ask wechat for user profile (Oauth) 3. create user
	 * 4. return userId (response body) and token (response cookie) Verify the
	 * SMS code and register the user, return token
	 * 
	 * @return userId String
	 */
	@RequestMapping(value = Constants.USER_REGISTER_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public User register(@RequestParam(value = "code", required = false) String code, @RequestBody User user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setSecurityCode(code);
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		User createdUser = userService.register(securityCode, user);
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, createdUser);
		return createdUser;
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable(Constants.USER_ID_KEY) String userId) {
		return userService.getUserById(userId);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me", method = RequestMethod.GET)
	public User getMe() {
		User sessionUser = UserSessionManager.getSessionUser();
		return userService.getUserById(sessionUser.getId());
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
	public User updateUser(@RequestBody User user) {
		return userService.update(user);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me", method = RequestMethod.PUT)
	public User updateMe(@RequestBody User user) {
		User sessionUser = UserSessionManager.getSessionUser();
		user.setId(sessionUser.getId());
		return userService.update(user);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable(Constants.USER_ID_KEY) String userId) {
		userService.delete(userId);
	}

	/**
	 * Return the qrcode with url+token
	 */
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/qrcode", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getQRCode() {
		User sessionUser = UserSessionManager.getSessionUser();
		String userId = sessionUser.getId();
		byte[] qrcode = userService.getQRCode(userId);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(qrcode, httpHeaders, HttpStatus.CREATED);
	}
}