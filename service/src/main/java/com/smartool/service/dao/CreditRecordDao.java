package com.smartool.service.dao;

import java.util.List;

import com.smartool.common.dto.CreditRecord;

public interface CreditRecordDao {
	CreditRecord createCreditRecord(CreditRecord creditRecord);

	CreditRecord getCreditRecord(String creditRecordId);

	CreditRecord updateCreditRecordStatus(String creditRecordId, int status);

	void removeCreditRecord(String creditRecordId);

	List<CreditRecord> listCreditRecords(String userId, Long start, Long end);

	List<CreditRecord> listCreditRecordsByMobileNumber(String siteId, String mobileNumber, Long start, Long end);
}
