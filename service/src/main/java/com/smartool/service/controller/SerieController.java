package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.EventDef;
import com.smartool.common.dto.EventSerieDef;
import com.smartool.common.dto.Serie;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.EventDefDao;
import com.smartool.service.dao.EventSerieDefDao;
import com.smartool.service.dao.SerieDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class SerieController {

	@Autowired
	SerieDao serieDao;
	
	@Autowired
	EventSerieDefDao eventSerieDefDao;

	@Autowired
	EventDefDao eventDefDao;
	/**
	 * CREATE
	 * 
	 * @RequestMapping(value = "/series")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Serie create(@PathVariable String eventTypeId, @RequestBody Serie serie) {
		serie.setId(CommonUtils.getRandomUUID());
		return serieDao.create(serie);
	}

	/**
	 * CREATE
	 * 
	 * @RequestMapping(value = "/series")
	 
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/series", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Serie> getSeries() {
		return serieDao.list();
	}*/

	/**
	 * GET
	 * 
	 * @RequestMapping(value = "/series/{SerieId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series/{serieId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Serie getSeries(@PathVariable String eventTypeId, @PathVariable String serieId) {
		return serieDao.get(serieId);
	}

	/**
	 * DELETE
	 * 
	 * @RequestMapping(value = "/Series/{SerieId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series/{serieId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String eventTypeId, @PathVariable String serieId) {
		serieDao.delete(serieId);
	}
	/**
	 * DELETE ALL
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}/series")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series", method = RequestMethod.DELETE)
	public void deleteAll(@PathVariable String eventTypeId) {
		serieDao.batchDeleteByEventType(eventTypeId);
	}
	/**
	 * LIST
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public List<Serie> getSeriesByEventTypeId(@PathVariable String eventTypeId) {
		return serieDao.list(eventTypeId);
	}

	/**
	 * UPDATE
	 * 
	 * @RequestMapping(value = "/eventtypes/{eventTypeId}/series/{serieId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series/{serieId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Serie update(@PathVariable String eventTypeId, @PathVariable String serieId, @RequestBody Serie serie) {
		return serieDao.update(serie);
	}
	
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventSerieDefs", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<EventSerieDef> listEventSerieDefs(){
		return eventSerieDefDao.listAll();
	}
	
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/eventtypes/{eventTypeId}/series/{serieId}/eventdefs", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<EventDef> getEventDefsBySerieId(@PathVariable String serieId){
		return eventDefDao.listEventDefBySerieId(serieId);
	}
}
