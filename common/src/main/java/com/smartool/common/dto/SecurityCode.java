package com.smartool.common.dto;

public class SecurityCode extends BaseDateTrackingBean {
	private String id;
	private String securityCode;
	private String mobileNumber;
	private String remoteAddr;
	private boolean isMobileExisted;

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isMobileExisted() {
		return isMobileExisted;
	}

	public void setMobileExisted(boolean isMobileExisted) {
		this.isMobileExisted = isMobileExisted;
	}
}
