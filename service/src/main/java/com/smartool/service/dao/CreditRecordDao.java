package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.CreditRecord;

public interface CreditRecordDao {
	CreditRecord createCreditRecord(CreditRecord creditRecord);

	List<CreditRecord> listCreditRecords(String userId, Long start, Long end);

	List<CreditRecord> listCreditRecordsByMobileNumber(String mobileNumber, Long start, Long end);
}
