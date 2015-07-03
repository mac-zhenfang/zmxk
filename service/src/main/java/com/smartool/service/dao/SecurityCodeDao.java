package com.smartool.service.dao;

import com.smartool.common.dto.SecurityCode;

public interface SecurityCodeDao {
	SecurityCode getSecurityCodeByMobileNumber(String mobileNumber);

	SecurityCode create(SecurityCode securityCode);

	SecurityCode update(SecurityCode securityCode);

	void sendSecurityCodeThoughSms(SecurityCode securityCode);

	void remove(String mobileNumber);
}
