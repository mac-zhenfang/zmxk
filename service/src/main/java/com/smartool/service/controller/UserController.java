package com.smartool.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.Cover;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.Team;
import com.smartool.common.dto.Track;
import com.smartool.common.dto.User;
import com.smartool.service.Constants;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.TeamDao;
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

	@Autowired
	KidDao kidDao;

	@Autowired
	TeamDao teamDao;

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
	public MappingJacksonValue login(@RequestBody LoginUser user, @RequestParam(required = false) String callback) {
		User existUser = userService.login(user);
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, existUser);
		MappingJacksonValue value = new MappingJacksonValue(existUser);
		value.setJsonpFunction(callback);
		return value;
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
	public MappingJacksonValue register(@RequestParam(value = "code", required = false) String code, @RequestParam(required = false) String callback, @RequestBody LoginUser user) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setSecurityCode(code);
		securityCode.setMobileNumber(user.getMobileNum());
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		User createdUser = userService.register(securityCode, user);
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, createdUser);
		MappingJacksonValue value = new MappingJacksonValue(createdUser);
		value.setJsonpFunction(callback);
		return value;
	}

	/**
	 * Get code
	 */
	@RequestMapping(value = Constants.GET_SECURITY_CODE_PATH, method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public MappingJacksonValue getSecurityCode(@RequestParam String mobileNum, @RequestParam(required = false) String callback) {
		SecurityCode securityCode = new SecurityCode();
		securityCode.setMobileNumber(mobileNum);
		securityCode.setRemoteAddr(httpServletRequest.getRemoteAddr());
		userService.getSecurityCode4Login(securityCode);
		MappingJacksonValue value = new MappingJacksonValue(securityCode);
		value.setJsonpFunction(callback);
		return value;
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
		//newCode.setSecurityCode("");
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

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public MappingJacksonValue getUser(@PathVariable(Constants.USER_ID_KEY) String userId, @RequestParam(required = false) String callback) {
		User user =  userService.getUserById(userId);
		MappingJacksonValue value = new MappingJacksonValue(user);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/grades", method = RequestMethod.GET)
	public MappingJacksonValue getMyGrades(@RequestParam(required = false) String callback) {
		User user = UserSessionManager.getSessionUser();
		List<Grade> grades = userService.getGrades(user.getId(), null);
		MappingJacksonValue value = new MappingJacksonValue(grades);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/kids/{kidId}/grades", method = RequestMethod.GET)
	public MappingJacksonValue getUserGrades(@PathVariable String userId, @PathVariable String kidId, @RequestParam(required = false) String callback) {
		List<Grade> grades = userService.getGrades(userId, kidId);
		MappingJacksonValue value = new MappingJacksonValue(grades);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/kids/{kidId}/tracks", method = RequestMethod.GET)
	public MappingJacksonValue getUserTracks(@PathVariable String userId, @PathVariable String kidId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "start", required = false) Integer start, @RequestParam(required = false) String callback) {
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = Integer.MAX_VALUE;
		}
		List<Track> tracks = userService.getTracks(userId, kidId, start, limit);
		MappingJacksonValue value = new MappingJacksonValue(tracks);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me/covers", method = RequestMethod.GET)
	public MappingJacksonValue getUserCovers(@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(required = false) String callback) {
		User user = UserSessionManager.getSessionUser();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = Integer.MAX_VALUE;
		}
		String userId = user.getId();
		List<Cover> covers = userService.getUserCovers(userId, start, limit);
		MappingJacksonValue value = new MappingJacksonValue(covers);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/kids/{kidId}/likes", method = RequestMethod.PUT)
	public void like(@PathVariable String userId, @PathVariable String kidId) {
		User user = UserSessionManager.getSessionUser();
		userService.like(userId, kidId, user.getId());
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}/kids/{kidId}/teams", method = RequestMethod.GET)
	public List<Team> getTeams(@PathVariable String userId, @PathVariable String kidId) {
		User user = userService.getUserById(userId);
		List<Kid> kids = user.getKids();
		boolean contains = false;
		for (Kid kid : kids) {
			if (kid.getId().equals(kidId)) {
				contains = true;
			}
		}
		if (!contains) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.NOT_ALOW_MANIPULATE_TEAM);
		}
		List<String> teamStringLst = kidDao.getTeams(kidId);
		List<Team> teamLst = new ArrayList<>();
		for (String id : teamStringLst) {
			Team team = teamDao.get(id);
			teamLst.add(team);
		}
		return teamLst;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me", method = RequestMethod.GET)
	public MappingJacksonValue getMe(@RequestParam(required = false) String callback) {
		User sessionUser = UserSessionManager.getSessionUser();
		User retUser = userService.getUserById(sessionUser.getId());
		MappingJacksonValue value = new MappingJacksonValue(retUser);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
	public MappingJacksonValue updateUser(@RequestBody User user, @RequestParam(required = false) String callback) {
		User me = UserSessionManager.getSessionUser();
		if (!me.getId().equals(user.getId())) {
			throw new SmartoolException(HttpStatus.UNAUTHORIZED.value(), ErrorMessages.NOT_AUTHORIZED_TO_UPDATE_USER);
		}
		User updateUser = userService.update(user);
		MappingJacksonValue value = new MappingJacksonValue(updateUser);
		value.setJsonpFunction(callback);
		return value;
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/users/me", method = RequestMethod.PUT)
	public MappingJacksonValue updateMe(@RequestBody User user, @RequestParam(required = false) String callback) {
		User sessionUser = UserSessionManager.getSessionUser();
		user.setId(sessionUser.getId());
		User updateUser = userService.update(user);
		MappingJacksonValue value = new MappingJacksonValue(updateUser);
		value.setJsonpFunction(callback);
		return value;
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
	public MappingJacksonValue getRanks(@RequestParam(required = false) String callback) {
		User sessionUser = UserSessionManager.getSessionUser();
		List<BaseGrade> baseGrades = userService.getRanks(sessionUser);
		MappingJacksonValue value = new MappingJacksonValue(baseGrades);
		value.setJsonpFunction(callback);
		return value;
	}
}