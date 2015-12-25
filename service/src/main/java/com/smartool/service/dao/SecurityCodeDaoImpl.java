package com.smartool.service.dao;

import java.io.StringWriter;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.SecurityCode;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.sms.SmsClient;

public class SecurityCodeDaoImpl implements SecurityCodeDao {
	
	private static Logger logger = Logger.getLogger(SecurityCodeDaoImpl.class);
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private SmsClient smsClient;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private SmartoolServiceConfig config;

	@Override
	public SecurityCode getSecurityCodeByMobileNumber(String mobileNumber) {
		return sqlSession.selectOne("SECURITY_CODE.getByMobileNumber", mobileNumber);
	}

	// @Override
	// public SecurityCode getSecurityCodeByRemoteAddr(String remoteAddr) {
	// return sqlSession.selectOne("SECURITY_CODE.getByRemoteAddr", remoteAddr);
	// }

	@Override
	public SecurityCode create(SecurityCode securityCode) {
		sqlSession.insert("SECURITY_CODE.create", securityCode);
		return sqlSession.selectOne("SECURITY_CODE.getByMobileNumber", securityCode.getMobileNumber());
	}

	@Override
	public void remove(String mobileNumber) {
		sqlSession.delete("SECURITY_CODE.remove", mobileNumber);
	}

	@Override
	public SecurityCode update(SecurityCode securityCode) {
		sqlSession.update("SECURITY_CODE.update", securityCode);
		return sqlSession.selectOne("SECURITY_CODE.getByMobileNumber", securityCode.getMobileNumber());
	}

	@Override
	public void sendSecurityCode(SecurityCode securityCode) {
		if (config.isSmsSendSecurityCodeEnabled()) {
			Template t = velocityEngine.getTemplate("templates/sms.vm", "UTF-8");
			VelocityContext context = new VelocityContext();
			context.put("securityCode", securityCode.getSecurityCode());
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			String msg = writer.toString();

			// FIXME Retry Template
			for (int i = 0; i < 3; i++) {
				if (smsClient.send(securityCode.getMobileNumber(), msg)) {
					return;
				}
			}
		} else {
			logger.info(" disalbed sms send");
		}
	}
}
