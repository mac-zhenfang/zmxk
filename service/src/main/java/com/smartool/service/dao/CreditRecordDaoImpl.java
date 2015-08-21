package com.smartool.service.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.common.dto.CreditRecord;

public class CreditRecordDaoImpl implements CreditRecordDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public CreditRecord createCreditRecord(CreditRecord creditRecord) {
		sqlSession.insert("CREDIT_RECORD.create", creditRecord);
		return getCreditRecord(creditRecord.getId());
	}

	@Override
	public CreditRecord getCreditRecord(String creditRecordId) {
		return sqlSession.selectOne("CREDIT_RECORD.getById", creditRecordId);
	}

	@Override
	public List<CreditRecord> listCreditRecordsByMobileNumber(String siteId, String mobileNum, Long start, Long end) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (siteId != null) {
			map.put("siteId", siteId);
		}
		if (start != null) {
			map.put("start", new Date(start));
		}
		if (end != null) {
			map.put("end", new Date(end));
		}
		if (mobileNum != null) {
			map.put("mobileNum", mobileNum);
		}
		return sqlSession.selectList("CREDIT_RECORD.search", map);
	}

	@Override
	public List<CreditRecord> listCreditRecords(String userId, Long start, Long end) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (start != null) {
			map.put("start", new Date(start));
		}
		if (end != null) {
			map.put("end", new Date(end));
		}
		if (userId != null) {
			map.put("userId", userId);
		}
		return sqlSession.selectList("CREDIT_RECORD.search", map);
	}

	@Override
	public CreditRecord updateCreditRecordStatus(String creditRecordId, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", creditRecordId);
		map.put("status", status);
		sqlSession.selectList("CREDIT_RECORD.updateStatus", map);
		return getCreditRecord(creditRecordId);
	}

	@Override
	public void removeCreditRecord(String creditRecordId) {
		sqlSession.selectList("CREDIT_RECORD.remove");
	}

}
