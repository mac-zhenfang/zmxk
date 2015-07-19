package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Serie;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.SerieDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class SerieController {

	@Autowired
	SerieDao serieDao;

	/**
	 * List
	 * 
	 * @RequestMapping(value = "/series")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/series", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Serie create(@RequestBody Serie serie) {
		serie.setId(CommonUtils.getRandomUUID());
		return serieDao.create(serie);
	}

	/**
	 * CREATE
	 * 
	 * @RequestMapping(value = "/series")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/series", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Serie> getSeries() {
		return serieDao.list();
	}

	/**
	 * GET
	 * 
	 * @RequestMapping(value = "/series/{SerieId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/series/{serieId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Serie getSeries(@PathVariable String serieId) {
		return serieDao.get(serieId);
	}

	/**
	 * DELETE
	 * 
	 * @RequestMapping(value = "/Series/{SerieId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/series/{serieId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String serieId) {
		serieDao.delete(serieId);
	}

	/**
	 * UPDATE
	 * 
	 * @RequestMapping(value = "/Series/{SerieId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/series/{serieId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Serie update(@PathVariable String serieId, @RequestBody Serie serie) {
		return serieDao.update(serie);
	}

}
