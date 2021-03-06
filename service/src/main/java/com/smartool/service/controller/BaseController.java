package com.smartool.service.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.smartool.service.SmartoolException;

@Transactional
public class BaseController {
	private static Logger logger = Logger.getLogger(BaseController.class);
	private static final String DEFAULT_ERROR_MESSAGE = "Unknown";

	@ExceptionHandler(SmartoolException.class)
	public ResponseEntity<Map<String, String>> handleSmartoolException(HttpServletRequest req, SmartoolException ex) {
		logger.warn("Caught exception: ", ex);
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", ex.getMessage());
		try {
			return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.valueOf(ex.getErrorCode()));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleBadRequest(HttpServletRequest req, Exception ex) {
		logger.error("Caught exception: ", ex);
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", DEFAULT_ERROR_MESSAGE);
		return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
