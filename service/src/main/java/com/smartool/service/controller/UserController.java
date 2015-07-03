package com.smartool.service.controller;

import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.Constants;
import com.smartool.service.SmartoolException;
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

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody User getUser(@PathVariable String userId,
			@CookieValue(Constants.KEY_FOR_USER_ID) String fooCookie) {
		System.out.println("Token cookie : " + fooCookie);
		System.out.println("userId : " + userId);
		User user = userDao.getUserById(userId);
		return user;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userDao.listAllUser();
	}

	/**
	 * 1. verify sms code 2. ask wechat for user profile (Oauth) 3. create user
	 * 4. return userId (response body) and token (response cookie) Verify the
	 * SMS code and register the user, return token
	 * 
	 * @return userId String
	 */
	@RequestMapping(value = "/users/register", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<User> register(@RequestParam(value = "code", required = false) String securityCode,
			@RequestBody User user) {
		// Validate fields (no duplicated mobileNum/wcId ?)
		if (!isValidSecurityCode(user.getMobileNum(), securityCode)) {
			return new ResponseEntity<User>(null, null, HttpStatus.UNAUTHORIZED);
		}
		user.setId(CommonUtils.getRandomUUID());
		User createdUser = userDao.createUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", Constants.KEY_FOR_USER_ID + "=" + createdUser.getId() + ";Max-Age=2592000;");
		return new ResponseEntity<User>(createdUser, headers, HttpStatus.OK);
	}

	private boolean isValidSecurityCode(String mobileNum, String securityCode) {
		SecurityCode getSecurityCode = securityCodeDao.getSecurityCodeByMobileNumber(mobileNum);
		return getSecurityCode.getSecurityCode().equals(securityCode);
	}

	/**
	 * 1. Get the SMS verify code 2. Verify captcha
	 */
	@RequestMapping(value = "/users/code", method = RequestMethod.POST)
	public String createCode(@RequestParam(value = "mobileNum", required = false) String mobileNumber) {
		SecurityCode existSecurityCode = securityCodeDao.getSecurityCodeByMobileNumber(mobileNumber);
		if (existSecurityCode != null) {
			Date lastModifiedTime = existSecurityCode.getLastModifiedTime();
			if (System.currentTimeMillis() - lastModifiedTime.getTime() < 30000l) {
				throw new SmartoolException(HttpStatus.NOT_ACCEPTABLE.value(),
						Constants.FREQUENT_REQUEST_SECURITY_CODE_ERROR_MESSAGE);
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
		QRCode.from("http://123456wechat.ngrok.io/service/smartool/api/v1/users/" + userId).withSize(800, 800)
				.to(ImageType.PNG).writeTo(out);
		byte[] qrcode = out.toByteArray();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(qrcode, httpHeaders, HttpStatus.CREATED);
	}
}