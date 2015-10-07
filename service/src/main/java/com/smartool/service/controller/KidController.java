package com.smartool.service.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartool.common.dto.Avatar;
import com.smartool.common.dto.Kid;
import com.smartool.service.CommonUtils;
import com.smartool.service.ErrorMessages;
import com.smartool.service.SmartoolException;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.KidDao;
import com.smartool.service.service.AvatarService;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class KidController extends BaseController {

	@Autowired
	private KidDao kidDao;

	@Autowired
	private AvatarService avatarService;

	@RequestMapping(value = "/users/{userId}/kids", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid create(@RequestBody Kid kid, @PathVariable String userId) {
		isUserValidKid(kid);
		kid.setId(CommonUtils.getRandomUUID());
		return kidDao.create(kid);
	}

	private boolean isUserValidKid(Kid kid) {
		if (CommonUtils.isEmptyString(kid.getName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.WRONG_KID_NAME_ERROR_MESSAGE);
		}
		if (CommonUtils.isEmptyString(kid.getSchoolName())) {
			throw new SmartoolException(HttpStatus.BAD_REQUEST.value(),
					ErrorMessages.WRONG_KID_SCHOOL_NAME_ERROR_MESSAGE);
		}
		return true;
	}

	@RequestMapping(value = "/users/{userId}/kids", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<Kid> getUsers(@PathVariable String userId) {
		List<Kid> kids = kidDao.listByUserId(userId);
		return kids;
	}

	@RequestMapping(value = "/users/{userId}/kids/{kidId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid update(@PathVariable String userId, @PathVariable String kidId, @RequestBody Kid kid) {
		// List<Kid> kids = kidDao.listByUserId(userId);
		// Check userId/kidId;
		isUserValidKid(kid);
		Kid retKid = kidDao.update(kid);
		return retKid;
	}

	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/users/{userId}/kids/{kidId}/avatar", method = RequestMethod.POST, consumes = {
			"multipart/*" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Avatar uploadKidAvatar(@PathVariable String userId, @PathVariable String kidId,
			@RequestPart("file") MultipartFile file) throws IOException {
		byte[] payload = file.getBytes();
		InputStream avatar = new ByteArrayInputStream(payload);
		return avatarService.upload(userId, kidId, avatar);
	}

	@RequestMapping(value = "/users/{userId}/kids/{kidId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String userId, @PathVariable String kidId) {
		// Check userId/kidId;
		kidDao.remove(kidId);
	}

	@RequestMapping(value = "/kids/{kidId}/joinTeam/{teamId}", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid joinTeam(@PathVariable String kidId, @PathVariable String teamId) {
		return kidDao.joinTeam(kidId, teamId);
	}

	@RequestMapping(value = "/kids/{kidId}/leaveTeam/{teamId}", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid leaveTeam(@PathVariable String kidId, @PathVariable String teamId) {
		return kidDao.leaveTeam(kidId, teamId);
	}
}
