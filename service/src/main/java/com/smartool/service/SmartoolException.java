package com.smartool.service;

public class SmartoolException extends RuntimeException {
	
	public SmartoolException(int errorCode, String message) {
		super(message);
	}

	public SmartoolException(int errorCode, String message, Exception cause) {
		super(message, cause);
	}
}
