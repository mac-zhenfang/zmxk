package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.EventType;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.EventTypeDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class EventTypeController {

	@Autowired
	EventTypeDao eventTypeDao;

	/**
	 * List
	 * 
	 * @RequestMapping(value = "/eventtypes")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public EventType create(@RequestBody EventType eventType) {
		eventType.setId(CommonUtils.getRandomUUID());
		return eventTypeDao.create(eventType);
	}

	/**
	 * CREATE
	 * 
	 * @RequestMapping(value = "/eventtypes")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventtypes", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<EventType> getEventTypes() {
		return eventTypeDao.list();
	}

	/**
	 * GET
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventtypes/{eventTypeId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public EventType getEventTypes(@PathVariable String eventTypeId) {
		return eventTypeDao.get(eventTypeId);
	}

	/**
	 * DELETE
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String eventTypeId) {
		eventTypeDao.delete(eventTypeId);
	}

	/**
	 * UPDATE
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public EventType update(@PathVariable String eventTypeId, @RequestBody EventType eventType) {
		return eventTypeDao.update(eventType);
	}

}
