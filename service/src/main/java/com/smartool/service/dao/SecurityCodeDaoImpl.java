package com.smartool.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.SecurityCode;

public class SecurityCodeDaoImpl implements SecurityCodeDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public SecurityCode getSecurityCodeByMobileNumber(String mobileNumber) {
		return sqlSession.selectOne("SECURITYCODE.getByMobileNumber", mobileNumber);
	}

	@Override
	public SecurityCode create(SecurityCode securityCode) {
		sqlSession.insert("SECURITYCODE.create", securityCode);
		return sqlSession.selectOne("SECURITYCODE.getByMobileNumber", securityCode.getMobileNumber());
	}

	@Override
	public void remove(String mobileNumber) {
		sqlSession.delete("SECURITYCODE.remove", mobileNumber);
	}

	@Override
	public SecurityCode update(SecurityCode securityCode) {
		sqlSession.update("SECURITYCODE.update", securityCode);
		return sqlSession.selectOne("SECURITYCODE.getByMobileNumber", securityCode.getMobileNumber());
	}

	@Override
	public void sendSecurityCodeThoughSms(SecurityCode securityCode) {
		// TODO Auto-generated method stub

	}

}
