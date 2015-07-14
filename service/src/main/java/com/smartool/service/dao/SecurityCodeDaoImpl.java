package com.smartool.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.SecurityCode;

public class SecurityCodeDaoImpl implements SecurityCodeDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public SecurityCode getSecurityCodeByMobileNumber(String mobileNumber) {
		return sqlSession.selectOne("SECURITY_CODE.getByMobileNumber", mobileNumber);
	}

	@Override
	public SecurityCode getSecurityCodeByRemoteAddr(String remoteAddr) {
		return sqlSession.selectOne("SECURITY_CODE.getByRemoteAddr", remoteAddr);
	}

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
	public void sendSecurityCodeThoughSms(SecurityCode securityCode) {
		// TODO Auto-generated method stub

	}
}
