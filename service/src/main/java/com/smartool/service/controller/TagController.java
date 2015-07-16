package com.smartool.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.common.dto.Tag;
import com.smartool.common.dto.User;
import com.smartool.service.CommonUtils;
import com.smartool.service.UserRole;
import com.smartool.service.controller.annotation.ApiScope;
import com.smartool.service.dao.TagDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class TagController extends BaseController {

	@Autowired
	private TagDao tagDao;

	@Transactional
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/tags", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Tag create(@RequestBody Tag tag) {
		tag.setId(CommonUtils.getRandomUUID());
		return tagDao.create(tag);
	}

	@ApiScope(userScope = UserRole.NORMAL_USER)
	@RequestMapping(value = "/tags/{tagId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Tag get(@PathVariable String tagId) {
		return tagDao.getTag(tagId);
	}

	@Transactional
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/tags/{tagId}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Tag create(@RequestBody Tag tag, @PathVariable String tagId) {
		//FIXME
		return tagDao.update(tag);
	}

	@ApiScope(userScope = UserRole.INTERNAL_USER)
	@RequestMapping(value = "/tags/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	List<Tag> search(@RequestParam(value = "type", required = false) String type) {
		return tagDao.getTags(type);
	}
	
	@Transactional
	@ApiScope(userScope = UserRole.ADMIN)
	@RequestMapping(value = "/tags", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Tag> list() {
		return tagDao.getTags();
	}
}
