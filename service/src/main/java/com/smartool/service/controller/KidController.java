package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Kid;
import com.smartool.service.CommonUtils;
import com.smartool.service.dao.KidDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class KidController extends BaseController {

	@Autowired
	private KidDao kidDao;

	@Transactional
	@RequestMapping(value = "/users/{userId}/kids", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Kid create(@RequestBody Kid kid, @PathVariable String userId) {
		// isUserValidForCreate(user);
		kid.setId(CommonUtils.getRandomUUID());
		return kidDao.create(kid);
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
		//List<Kid> kids = kidDao.listByUserId(userId);
		//Check userId/kidId;
		Kid retKid = kidDao.update(kid);
		return retKid;
	}
	
	@RequestMapping(value = "/users/{userId}/kids/{kidId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String userId, @PathVariable String kidId) {
		//Check userId/kidId;
		kidDao.remove(kidId);
	}
}
