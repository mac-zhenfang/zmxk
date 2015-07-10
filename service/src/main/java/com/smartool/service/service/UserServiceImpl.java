package com.smartool.service.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.dao.SecurityCodeDao;
import com.smartool.service.dao.UserDao;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SecurityCodeDao securityCodeDao;

	@Override
	public List<User> listAllUser() {
		return userDao.listAllUser();
	}

	@Override
	public User login(SecurityCode securityCode, User user) {
		if (securityCode.getSecurityCode() == null) {
			validateMobileNumberForLogin(securityCode.getMobileNumber());
			String securityCodeString = createCodeForLogin(securityCode);
			// TODO Temporary solution before implement send code though SMS
			throw new SmartoolException(HttpStatus.NOT_ACCEPTABLE.value(), securityCodeString);
		}

		if (!isValidSecurityCode(securityCode)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
		}
		User existUser = userDao.getUserByMobileNumber(user.getMobileNum());
		if (existUser != null) {
			return existUser;
		}
		throw new SmartoolException(HttpStatus.NOT_FOUND.value(),
				ErrorMessages.INVALID_MOBILE_NUMBER_OR_PASSWORD_ERROR_MESSAGE);
	}

	private String createCodeForLogin(SecurityCode securityCode) {
		SecurityCode existSecurityCode = getExistedSecurityCode(securityCode);
		if (existSecurityCode != null) {
			Date lastModifiedTime = existSecurityCode.getLastModifiedTime();
			if (System.currentTimeMillis() - lastModifiedTime.getTime() < 30000l) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
						ErrorMessages.FREQUENT_REQUEST_SECURITY_CODE_ERROR_MESSAGE);
			} else {
				existSecurityCode.setSecurityCode(CommonUtils.getRandomSecurityCode());
				existSecurityCode.setMobileNumber(securityCode.getMobileNumber());
				existSecurityCode.setRemoteAddr(securityCode.getRemoteAddr());
				securityCodeDao.sendSecurityCodeThoughSms(existSecurityCode);
				return securityCodeDao.update(existSecurityCode).getSecurityCode();
			}
		} else {
			securityCode.setSecurityCode(CommonUtils.getRandomSecurityCode());
			securityCodeDao.sendSecurityCodeThoughSms(securityCode);
			return securityCodeDao.create(securityCode).getSecurityCode();
		}
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
		if (CommonUtils.isEmptyString(user.getLocation())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_LOCATION_ERROR_MESSAGE);
		}
		// Phone number/ WC id not registered before
		if (userDao.getUserByMobileNumber(user.getMobileNum()) != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
		}
		if (user.getKids() != null && !user.getKids().isEmpty()) {
			for (Kid kid : user.getKids()) {
				isKidValidForCreate(kid);
			}
		}
		return true;
	}

	public boolean validateMobileNumberForRegister(String mobileNumber) {
		if (CommonUtils.isEmptyString(mobileNumber)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_MOBILE_NUMBER_ERROR_MESSAGE);
		}
		if (userDao.getUserByMobileNumber(mobileNumber) != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
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

	private boolean isValidSecurityCode(SecurityCode securityCode) {
		if (securityCode == null) {
			return false;
		}
		SecurityCode getSecurityCode = securityCodeDao.getSecurityCodeByMobileNumber(securityCode.getMobileNumber());
		return getSecurityCode != null && getSecurityCode.getSecurityCode() != null
				&& getSecurityCode.getSecurityCode().equals(securityCode.getSecurityCode());
	}

	private String createCodeForRegister(SecurityCode securityCode) {
		User userByMobileNumber = userDao.getUserByMobileNumber(securityCode.getMobileNumber());
		if (userByMobileNumber != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
		}
		return createCodeForLogin(securityCode);
	}

	private SecurityCode getExistedSecurityCode(SecurityCode securityCode) {
		SecurityCode existedSecurityCode = securityCodeDao
				.getSecurityCodeByMobileNumber(securityCode.getMobileNumber());
		if (existedSecurityCode != null) {
			return existedSecurityCode;
		}
		String remoteAddr = securityCode.getRemoteAddr();
		return securityCodeDao.getSecurityCodeByRemoteAddr(remoteAddr);
	}

	public boolean validateMobileNumberForLogin(String mobileNumber) {
		if (CommonUtils.isEmptyString(mobileNumber)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_MOBILE_NUMBER_ERROR_MESSAGE);
		}
		if (userDao.getUserByMobileNumber(mobileNumber) == null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_HAVNT_REGISTERED_ERROR_MESSAGE);
		}
		return true;
	}

	@Override
	public User register(SecurityCode securityCode, User user) {
		if (securityCode.getSecurityCode() == null) {
			validateMobileNumberForRegister(securityCode.getMobileNumber());
			String securityCodeString = createCodeForRegister(securityCode);
			// TODO Temporary solution before implement send code though SMS
			throw new SmartoolException(HttpStatus.NOT_ACCEPTABLE.value(), securityCodeString);
		}

		if (!isValidSecurityCode(securityCode)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
		}
		isUserValidForCreate(user);
		user.setId(CommonUtils.getRandomUUID());
		user.setRoleId(UserRole.NORMAL_USER.getValue());
		// user.setPassword(CommonUtils.encryptBySha2(user.getPassword()));
		User createdUser = userDao.createUser(user);
		securityCodeDao.remove(user.getMobileNum());
		return createdUser;
	}

	@Override
	public User getUserById(String userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public byte[] getQRCode(String userId) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QRCode.from("http://123456wechat.ngrok.io/admin/index.html#/enroll/" + userId).withSize(800, 800)
				.to(ImageType.PNG).writeTo(out);
		return out.toByteArray();
	}

}
