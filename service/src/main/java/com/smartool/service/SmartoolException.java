package com.smartool.service;

public class SmartoolException extends RuntimeException {
	private static final long serialVersionUID = 916163809455877983L;
	private int errorCode = 500;

	public SmartoolException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public SmartoolException(int errorCode, String message, Exception cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
