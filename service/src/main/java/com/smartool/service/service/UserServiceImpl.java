package com.smartool.service.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.Cover;
import com.smartool.common.dto.EventType;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.Kid;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.Trophy;
import com.smartool.common.dto.User;
import com.smartool.common.dto.UserGrade;
import com.smartool.common.dto.UserStat;
import com.smartool.service.CommonUtils;
import com.smartool.service.Constants;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.UserSessionManager;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.EventTypeDao;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.SecurityCodeDao;
import com.smartool.service.dao.UserDao;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import redis.clients.jedis.Jedis;

public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private SecurityCodeDao securityCodeDao;
	@Autowired
	private KidDao kidDao;
	

	@Autowired
	private EventTypeDao eventTypeDao;

	@Autowired
	private SmartoolServiceConfig config;
	
	private final static Joiner joiner = Joiner.on(":");
	
	private final static Splitter splitter = Splitter.on(":");

	@Override
	public List<User> listAllUser() {
		List<User> users = userDao.listAllUser();
		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				List<Kid> kids = kidDao.listByUserId(user.getId());
				user.setKids(kids);
			}
		}
		return users;
	}

	@Override
	public User login(LoginUser user) {
		if(!Strings.isNullOrEmpty(user.getPassword())) {
			return internalPasswdLogin(user);
		} else {
			return internalSecurityCodeLogin(user);
		}
	}
	
	private User internalSecurityCodeLogin(LoginUser user) {
		if (CommonUtils.isEmptyString(user.getCode())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_SECURITY_CODE_ERROR_MESSAGE);
		} else {
			SecurityCode securityCode = new SecurityCode();
			securityCode.setMobileNumber(user.getMobileNum());
			securityCode.setSecurityCode(user.getCode());
			if (!isValidSecurityCode(securityCode)) {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
			}
			LoginUser existUser = userDao.getLoginUserByMobileNumber(user.getMobileNum());
			return userDao.getUserById(existUser.getId());
		}
	}
	private User internalPasswdLogin(LoginUser user) {

		if (CommonUtils.isEmptyString(user.getPassword())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_PASSWORD_ERROR_MESSAGE);
		}
		LoginUser existUser = userDao.getLoginUserByMobileNumber(user.getMobileNum());
		if (existUser != null) {
			if (CommonUtils.encryptBySha2(user.getPassword()).equals(existUser.getPassword())) {
				return userDao.getUserById(existUser.getId());
			} else {
				throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
						ErrorMessages.INVALID_MOBILE_NUMBER_OR_PASSWORD_ERROR_MESSAGE);
			}
		}
		throw new SmartoolException(HttpStatus.NOT_FOUND.value(),
				ErrorMessages.INVALID_MOBILE_NUMBER_OR_PASSWORD_ERROR_MESSAGE);
	
	}

	private String createCodeInternal(SecurityCode securityCode) {
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
				securityCodeDao.sendSecurityCode(existSecurityCode);
				return securityCodeDao.update(existSecurityCode).getSecurityCode();
			}
		} else {
			securityCode.setSecurityCode(CommonUtils.getRandomSecurityCode());
			securityCodeDao.sendSecurityCode(securityCode);
			return securityCodeDao.create(securityCode).getSecurityCode();
		}
	}

	private boolean isUserValidForCreate(LoginUser user) {
		// All string fields not empty
		if (CommonUtils.isEmptyString(user.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_USER_NAME_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(user.getPassword()) || user.getPassword().length() < 6) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_PASSWORD_ERROR_MESSAGE);
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
		// FIXME: check if it is a 11 number
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
		return getSecurityCode != null && !Strings.isNullOrEmpty(getSecurityCode.getSecurityCode())
				&& getSecurityCode.getSecurityCode().equals(securityCode.getSecurityCode());
	}

	private String createCodeForRegister(SecurityCode securityCode) {
		User userByMobileNumber = userDao.getUserByMobileNumber(securityCode.getMobileNumber());
		if (userByMobileNumber != null) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.MOBILE_NUMBER_ALREADY_USED_ERROR_MESSAGE);
		}
		return createCodeInternal(securityCode);
	}

	private SecurityCode getExistedSecurityCode(SecurityCode securityCode) {
		SecurityCode existedSecurityCode = securityCodeDao
				.getSecurityCodeByMobileNumber(securityCode.getMobileNumber());
		//if (existedSecurityCode != null) {
			return existedSecurityCode;
		//}
		//String remoteAddr = securityCode.getRemoteAddr();
		//return securityCodeDao.getSecurityCodeByRemoteAddr(remoteAddr);
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
	public User register(SecurityCode securityCode, LoginUser user) {
		validateMobileNumberForRegister(securityCode.getMobileNumber());
		if (!isValidSecurityCode(securityCode)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
		}
		
		if(Strings.isNullOrEmpty(user.getPassword()) && !config.needPassword()) {
			user.setPassword(config.getDefaultPassword());
			user.setIdp(Constants.USE_DEFAULT_PASSWORD);
		}
		
		isUserValidForCreate(user);
		user.setId(CommonUtils.getRandomUUID());
		user.setRoleId(UserRole.NORMAL_USER.getValue());
		user.setPassword(CommonUtils.encryptBySha2(user.getPassword()));
		User createdUser = userDao.createUser(user);
		securityCodeDao.remove(user.getMobileNum());
		return createdUser;
	}
	
	@Override
	public User getUserById(String userId) {
		User user = userDao.getUserById(userId);
		int likes = 0;
		if (user != null) {
			List<Kid> kids = kidDao.listByUserId(userId);
			for(Kid kid : kids) {
				likes+=kid.getLikes();
			}
			user.setKids(kids);
			user.setLikes(likes);
		}
		return user;
	}

	@Override
	public byte[] getQRCode(String userId) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StringBuilder sb = new StringBuilder();
		sb.append(config.getQrCodePath()).append("/admin/index.html#/enroll/").append(userId);
		QRCode.from(sb.toString()).withSize(config.getQrCodeLength(), config.getQrCodeWidth()).to(ImageType.PNG).writeTo(out);
		return out.toByteArray();
	}

	@Override
	public List<User> search(String mobileNum, String wcId, String kidName) {
		return userDao.search(mobileNum, wcId, kidName);
	}

	@Override
	public List<User> search(String query) {
		List<User> users = userDao.search(query);
		return users;
	}

	@Override
	public User update(User user) {
		User sessionUser = UserSessionManager.getSessionUser();
		User existedUser = userDao.getUserById(user.getId());
		if (existedUser == null) {
			throw new SmartoolException(HttpStatus.NOT_FOUND.value(), ErrorMessages.NOT_FOUND_ERROR_MESSAGE);
		}
		if (userRoleChaned(existedUser, user) && UserRole.ADMIN.getValue().compareTo(sessionUser.getRoleId()) > 0) {
			throw new SmartoolException(HttpStatus.FORBIDDEN.value(), ErrorMessages.FORBIDEN_ERROR_MESSAGE);
		}
		// user.setPassword(CommonUtils.encryptBySha2(user.getPassword()));
		return userDao.updateUser(user);
	}

	private boolean userRoleChaned(User existedUser, User user) {
		return !existedUser.getRoleId().equals(user.getRoleId());
	}

	@Override
	public void delete(String userId) {
		userDao.remove(userId);
	}

	@Override
	public List<Grade> getGrades(String userId, String kidId) {
		List<Grade> grades = userDao.getGrades(userId);
		List<Grade> returnGrades = new ArrayList<>();
		if(null == kidId) {
			return grades;
		}
		for(Grade grade : grades) {
			if(grade.getKidId().equals(kidId)) {
				returnGrades.add(grade);
			}
		}
		return returnGrades;
	}

	@Override
	public SecurityCode getSecurityCode(SecurityCode securityCode) {
		validateMobileNumberForRegister(securityCode.getMobileNumber());
		String securityCodeString = createCodeForRegister(securityCode);
		securityCode.setSecurityCode(securityCodeString);
		return securityCode;
	}
	


	@Override
	public List<BaseGrade> getRanks(User user) {
		List<BaseGrade> baseGrades = new ArrayList<>();
		List<EventType> eventTypes = eventTypeDao.getDistinctEventTypes(user.getId());
		for (EventType eventType : eventTypes) {
			baseGrades.addAll(userDao.getBaseGradesByEventType(eventType.getId(), 0, Integer.MAX_VALUE));
		}
		return baseGrades;
	}

	@Override
	public SecurityCode getSecurityCodeToSetPassword(SecurityCode securityCode) {
		validateMobileNumberForLogin(securityCode.getMobileNumber());
		String securityCodeString = createCodeInternal(securityCode);
		securityCode.setSecurityCode(securityCodeString);
		return securityCode;
	}

	@Override
	public User setPassword(SecurityCode securityCode, LoginUser user) {
		if (!isValidSecurityCode(securityCode)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_ERROR_CODE_ERROR_MESSAGE);
		}
		LoginUser existedUser = userDao.getLoginUserByMobileNumber(user.getMobileNum());
		existedUser.setPassword(CommonUtils.encryptBySha2(user.getPassword()));
		existedUser.setIdp(Constants.NOT_USE_DEFAULT_PASSWORD);
		LoginUser updateUser = userDao.updatePassword(existedUser);
		securityCodeDao.remove(user.getMobileNum());
		return updateUser;
	}

	@Override
	public User createUser(User user) {
		validateMobileNumberForRegister(user.getMobileNum());
		LoginUser loginUser = new LoginUser();
		loginUser.setId(CommonUtils.getRandomUUID());
		loginUser.setName(user.getName());
		loginUser.setMobileNum(user.getMobileNum());
		loginUser.setKids(user.getKids());
		loginUser.setLocation(user.getLocation());
		loginUser.setRoleId(UserRole.NORMAL_USER.getValue());
		loginUser.setPassword(CommonUtils.encryptBySha2(config.getDefaultPassword()));
		loginUser.setIdp(Constants.USE_DEFAULT_PASSWORD);
		isUserValidForCreate(loginUser);
		User createdUser = userDao.createUser(loginUser);
		securityCodeDao.remove(user.getMobileNum());
		return createdUser;
	}

	@Override
	public SecurityCode getSecurityCode4Login(SecurityCode securityCode) {
		//validateMobileNumberForRegister(securityCode.getMobileNumber());
		String mobileNumber = securityCode.getMobileNumber();
		boolean isMobileExisted;
		if (CommonUtils.isEmptyString(mobileNumber)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_MOBILE_NUMBER_ERROR_MESSAGE);
		}
		isMobileExisted = (userDao.getUserByMobileNumber(mobileNumber) != null);
		String securityCodeString = createCodeInternal(securityCode);
		//securityCode.setSecurityCode(securityCodeString);
		securityCode.setMobileExisted(isMobileExisted);
		return securityCode;
	}

	@Override
	public List<Cover> getUserCovers(String userId, int start, int limit) {
		List<BaseGrade> baseGrades = new ArrayList<>();
		List<EventType> eventTypes = eventTypeDao.getDistinctEventTypes(userId);
		for (EventType eventType : eventTypes) {
			baseGrades.addAll(userDao.getBaseGradesByEventType(eventType.getId(), start, limit));
		}
		Map<String, List<BaseGrade>> userGradeGroup = new HashMap<String, List<BaseGrade>>();
		for(BaseGrade grade : baseGrades){
			/*UserGrade userGrade = new UserGrade();
			userGrade.setId(grade.getUserId());
			userGrade.setName(grade.getUserName());
			//FIXME
			userGrade.setCenterLogoIcon("");
			userGrade.setKid(buildKid(grade));
			userGrade.setTotalAttendTimes(totalAttendTimes);*/
			StringBuilder sb = new StringBuilder();
			joiner.appendTo(sb, grade.getUserId(), grade.getEventTypeId(), grade.getSiteId(), grade.getKidId());
			if(!userGradeGroup.containsKey(sb.toString())) {
				userGradeGroup.put(sb.toString(), new ArrayList<BaseGrade>());
			}
			userGradeGroup.get(sb.toString()).add(grade);
		}
		List<Cover> returnUserGrades = new ArrayList<>();
		for(Entry<String, List<BaseGrade>> entry : userGradeGroup.entrySet()) {
			String id = entry.getKey();
			List<String> ids =  splitter.splitToList(id);
			String uid = ids.get(0);
			String eventTypeId = ids.get(1);
			String siteId = ids.get(2);
			String kidId = ids.get(3);
			Kid kid = kidDao.get(kidId);
			UserGrade userGrade = new UserGrade();
			
			UserStat userStat = userDao.getUserStat(uid);
			List<BaseGrade> gradesInEntry = entry.getValue();
			userGrade.setId(uid);
			userGrade.setKid(kid);
			userGrade.setEventTypeId(eventTypeId);
			userGrade.setSiteId(siteId);
			userGrade.setName(gradesInEntry.get(0).getUserName());
			userGrade.setCenterLogoIcon("");
			userGrade.setFatestTime(userStat.getFatestScore());
			userGrade.setTotalAttendTimes(userStat.getTotalAttend());
			userGrade.setTotalUserCredit(userStat.getCredit());
			//FIXME Credit * kids number != total user credit???
			List<Trophy> trophyList = new ArrayList<>();
			int credit =0;
			for(BaseGrade grade : gradesInEntry) {
				Trophy trophy = new Trophy();
				trophy.setStage(grade.getStage());
				trophy.setRank(grade.getRank());
				trophy.setRoundId(grade.getRoundId());
				trophy.setRoundLevel(grade.getRoundLevel());
				trophy.setRoundLevelName(grade.getRoundLevelName());
				credit+=grade.getCredit();
				trophyList.add(trophy);
			}
			userGrade.setCredit(credit);
			userGrade.setTrophyList(trophyList);
			returnUserGrades.add(userGrade);
		}
		return returnUserGrades;
	}

	@Override
	public void like(String toUserId, String toKidId, String fromUserId) {
		Kid kid = kidDao.get(toKidId);
		if(!kid.getUserId().equals(toUserId)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.NOT_ALLOWED_TO_LIKE_THIS_USER);
		}
		if(kid.getUserId().equals(fromUserId)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.NOT_ALLOWED_TO_LIKE_THIS_USER);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(toUserId).append("|").append(toKidId);
		if(userDao.existUserInLike(sb.toString(), fromUserId)) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.USER_ALREADY_LIKE_YOU);
		}
		userDao.incrLike(fromUserId, sb.toString());
	}
}
