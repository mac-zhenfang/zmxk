package com.smartool.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartool.service.dao.KidDao;

@RestController
@RequestMapping(value = "/smartool/api/v1")
public class KidController  extends BaseController {
	@Autowired
	private KidDao kidDao;
}
