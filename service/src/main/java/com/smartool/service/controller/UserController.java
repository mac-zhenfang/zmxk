package com.smartool.service.controller;

import java.util.List;
import java.util.Map;

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

import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.Cover;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.Team;
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
	@RequestMapping(value = "/users/query", method = RequestMethod.POST)
	public List<User> query(@RequestBody Map<String, String> criteria) {
		if (criteria == null) {
			return userService.search(null, null, null);
		}
		return userService.search(criteria.get("mobileNumber"), criteria.get("wcId"), criteria.get("kidName"));
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/users/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> search(@RequestParam(value = "query", required = false) String query) {
		return userService.search(query);
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userService.listAllUser();
	}

	
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}
	
	@RequestMapping(value = Constants.USER_LOGIN_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public User login(@RequestBody LoginUser user) {
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
	public User register(@RequestParam(value = "code", required = false) String code, @RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setSecurityCode(code);
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		User createdUser = userService.register(securityCode, user);
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, createdUser);
		return createdUser;
	}

	/**
	 * Get code
	 */
	@RequestMapping(value = Constants.GET_SECURITY_CODE_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void getSecurityCode(@RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		userService.getSecurityCode4Login(securityCode);
	}
	
	/**
	 * Get code
	 */
	@RequestMapping(value = "/users/codeforlogin", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SecurityCode getSecurityCodeForNewLogin(@RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		SecurityCode newCode = userService.getSecurityCode4Login(securityCode);
		newCode.setSecurityCode("");
		return newCode;
	}
	
	

	@RequestMapping(value = "/users/codeforsetpassword", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void getSecurityCodeToSetPassword(@RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		userService.getSecurityCodeToSetPassword(securityCode);
	}

	@RequestMapping(value = "/users/setpassword", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public User setPassword(@RequestParam(value = "code", required = false) String code, @RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setSecurityCode(code);
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		return userService.setPassword(securityCode, user);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable(Constants.USER_ID_KEY) String userId) {
		return userService.getUserById(userId);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/grades", method = RequestMethod.GET)
	public List<Grade> getMyGrades() {
		User user = UserSessionManager.getSessionUser();
		List<Grade> grades = userService.getGrades(user.getId());
		return grades;
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/grades", method = RequestMethod.GET)
	public List<Grade> getUserGrades() {
		User user = UserSessionManager.getSessionUser();
		// FIXME, get user site
		List<Grade> grades = userService.getGrades(user.getId());
		return grades;
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/covers", method = RequestMethod.GET)
	public List<Cover> getUserCovers(@PathVariable String userId){
		return userService.getUserCovers(userId);
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/likes", method = RequestMethod.PUT)
	public void like() {
		//TODO
		//put into redis
		//get from redis
		//every 2 min, append all user likes to DB
	}
	
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/teams", method = RequestMethod.GET)
	public List<Team> getTeams(){
		//TODO
		return null;
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
	 * Return the teams my kids belongs to
	 *
	 * @RequestMapping(value = "/users/me/teams", method = RequestMethod.GET,
	 *                       produces = { MediaType.APPLICATION_JSON_VALUE })
	 *                       public List<Team> getMyTeams() { User sessionUser =
	 *                       UserSessionManager.getSessionUser(); List
	 *                       <Team> teams =
	 *                       teamDao.memberOf(sessionUser.getId()); return
	 *                       teamDao.list(); }
	 */

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

	/**
	 * Return the qrcode with url+token
	 */
	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/ranks", method = RequestMethod.GET)
	public List<BaseGrade> getRanks() {
		User sessionUser = UserSessionManager.getSessionUser();
		List<BaseGrade> baseGrades = userService.getRanks(sessionUser);
		return baseGrades;
	}
}