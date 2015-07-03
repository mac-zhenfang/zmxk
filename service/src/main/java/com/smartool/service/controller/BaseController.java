package com.smartool.service.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.smartool.service.SmartoolException;

public class BaseController {
	private static final String DEFAULT_ERROR_MESSAGE = "Unknown";

	@ExceptionHandler(SmartoolException.class)
	@ResponseBody
	ResponseEntity<Map<String, String>> handleSmartoolException(HttpServletRequest req, SmartoolException ex) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", ex.getMessage());
		try {
			return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.valueOf(ex.getErrorCode()));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	ResponseEntity<Map<String, String>> handleBadRequest(HttpServletRequest req, Exception ex) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", DEFAULT_ERROR_MESSAGE);
		return new ResponseEntity<Map<String, String>>(map, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}