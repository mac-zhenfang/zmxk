package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Site;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.SiteDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class SiteController {

	@Autowired
	SiteDao siteDao;

	/**
	 * List
	 * 
	 * @RequestMapping(value = "/sites")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/sites", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Site create(@RequestBody Site site) {
		site.setId(CommonUtils.getRandomUUID());
		return siteDao.create(site);
	}

	/**
	 * CREATE
	 * 
	 * @RequestMapping(value = "/sites")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/sites", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Site> getSites() {
		return siteDao.list();
	}

	/**
	 * GET
	 * 
	 * @RequestMapping(value = "/sites/{SiteId}")
	 */
	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/sites/{siteId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Site getSites(@PathVariable String siteId) {
		return siteDao.get(siteId);
	}

	/**
	 * DELETE
	 * 
	 * @RequestMapping(value = "/Sites/{SiteId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/sites/{siteId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String siteId) {
		siteDao.delete(siteId);
	}

	/**
	 * UPDATE
	 * 
	 * @RequestMapping(value = "/Sites/{SiteId}")
	 */
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/sites/{siteId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Site update(@PathVariable String siteId, @RequestBody Site site) {
		return siteDao.update(site);
	}

}
