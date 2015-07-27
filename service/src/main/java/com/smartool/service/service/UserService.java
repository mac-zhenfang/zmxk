package com.smartool.service.service;

import java.util.List;

import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.SecurityCode;
import com.smartool.common.dto.User;

public interface UserService {
	List<User> listAllUser();

	User login(LoginUser user);

	User register(SecurityCode securityCode, LoginUser user);

	User getUserById(String userId);

	byte[] getQRCode(String userId);
	
	List<BaseGrade> getRanks(User user);
	
	List<User> search(String query);
	
	User update(User user);
	
	void delete(String userId);
	
	List<Grade> getGrades(String userId);
	
	SecurityCode getSecurityCode(SecurityCode securityCode);
	
}
