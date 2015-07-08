package com.smartool.service.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.Constants;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.dao.SecurityCodeDao;
import com.smartool.service.dao.UserDao;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class UserController extends BaseController {
	@Autowired
	private UserDao userDao;
	@Autowired
	private SecurityCodeDao securityCodeDao;
	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	@Autowired
	private HttpServletResponse httpServletResponse;

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getUser(@PathVariable String userId,
			@CookieValue(Constants.KEY_FOR_USER_TOKEN) String fooCookie) {
		User user = userDao.getUserById(userId);
		return user;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		List<User> users = userDao.listAllUser();
		return users;
	}

	@Transactional
	@RequestMapping(value = Constants.USER_LOGIN_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public User login(@RequestBody User user) {
		// TODO
//		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, createdUser);
		return null;
	}

	/**
	 * 1. verify sms code 2. ask wechat for user profile (Oauth) 3. create user
	 * 4. return userId (response body) and token (response cookie) Verify the
	 * SMS code and register the user, return token
	 * 
	 * @return userId String
	 */
	@Transactional
	@RequestMapping(value = Constants.USER_REGISTER_PATH, method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public User register(@RequestParam(value = "code", required = false) String securityCode, @RequestBody User user) {
		if (!isValidSecurityCode(user.getMobileNum(), securityCode)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
		}
		isUserValidForCreate(user);
		user.setId(CommonUtils.getRandomUUID());
		user.setRoleId(UserRole.NORMAL_USER.getValue());
		user.setPassword(CommonUtils.encryptBySha2(user.getPassword()));
		User createdUser = userDao.createUser(user);
		securityCodeDao.remove(user.getMobileNum());
		authenticationInterceptor.addCookieIntoResponse(httpServletResponse, createdUser);
		return createdUser;
	}

	private boolean isUserValidForCreate(User user) {
		// All string fields not empty
		if (CommonUtils.isEmptyString(user.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_USER_NAME_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(user.getMobileNum())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_MOBILE_NUMBER_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(user.getWcId())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_WC_ID_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(user.getLocation())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_LOCATION_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(user.getPassword())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_PASSWORD_ERROR_MESSAGE);
		}
		// Phone number/ WC id not registered before
		if (userDao.getUserByMobileNumber(user.getMobileNum()) != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
		}
		if (userDao.getUserByWcId(user.getWcId()) != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WC_ID_ALREADY_USED_ERROR_MESSAGE);
		}
		if (user.getKids() != null && !user.getKids().isEmpty()) {
			for (Kid kid : user.getKids()) {
				isKidValidForCreate(kid);
			}
		}
		return true;
	}

	private void isKidValidForCreate(Kid kid) {
		if (CommonUtils.isEmptyString(kid.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_KID_NAME_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(kid.getSchoolName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_KID_SCHOOL_NAME_ERROR_MESSAGE);
		}
	}

	// private boolean isUserValidForUpdate(User user) {
	// return true;
	// }

	private boolean isValidSecurityCode(String mobileNum, String securityCode) {
		SecurityCode getSecurityCode = securityCodeDao.getSecurityCodeByMobileNumber(mobileNum);
		return getSecurityCode != null && getSecurityCode.getSecurityCode() != null
				&& getSecurityCode.getSecurityCode().equals(securityCode);
	}

	/**
	 * 1. Get the SMS verify code 2. Verify captcha
	 */
	@Transactional
	@RequestMapping(value = Constants.GET_SECURITY_CODE_PATH, method = RequestMethod.POST)
	public String createCode(@RequestParam(value = "mobileNum", required = false) String mobileNumber) {
		User userByMobileNumber = userDao.getUserByMobileNumber(mobileNumber);
		if (userByMobileNumber != null) {
			throw new SmartoolException(HttpStatus.NOT_ACCEPTABLE.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
		}
		SecurityCode existSecurityCode = securityCodeDao.getSecurityCodeByMobileNumber(mobileNumber);
		if (existSecurityCode != null) {
			Date lastModifiedTime = existSecurityCode.getLastModifiedTime();
			if (System.currentTimeMillis() - lastModifiedTime.getTime() < 30000l) {
				throw new SmartoolException(HttpStatus.NOT_ACCEPTABLE.value(),
						ErrorMessages.FREQUENT_REQUEST_SECURITY_CODE_ERROR_MESSAGE);
			} else {
				existSecurityCode.setSecurityCode(CommonUtils.getRandomSecurityCode());
				securityCodeDao.sendSecurityCodeThoughSms(existSecurityCode);
				return securityCodeDao.update(existSecurityCode).getSecurityCode();
			}
		} else {
			SecurityCode securityCode = new SecurityCode();
			securityCode.setMobileNumber(mobileNumber);
			securityCode.setSecurityCode(CommonUtils.getRandomSecurityCode());
			securityCodeDao.sendSecurityCodeThoughSms(existSecurityCode);
			return securityCodeDao.create(securityCode).getSecurityCode();
		}
	}

	/**
	 * Return the qrcode with url+token
	 */
	// TODO
	@RequestMapping(value = "/users/{userId}/qrcode", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getQRCode(@PathVariable String userId) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QRCode.from("http://123456wechat.ngrok.io/admin/index.html#?page=eventEnroll&userId=" + userId)
				.withSize(800, 800).to(ImageType.PNG).writeTo(out);
		byte[] qrcode = out.toByteArray();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(qrcode, httpHeaders, HttpStatus.CREATED);
	}
}