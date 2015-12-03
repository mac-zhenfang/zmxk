package com.smartool.service.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smartool.common.dto.BaseGrade;
import com.smartool.common.dto.CreditRecord;
import com.smartool.common.dto.CreditRule;
import com.smartool.common.dto.Grade;
import com.smartool.common.dto.LoginUser;
import com.smartool.common.dto.User;
import com.smartool.common.dto.UserStat;

public interface UserDao {
	User createUser(LoginUser user);

	User getUserById(String userId);

	User getUserByMobileNumber(String mobileNumber);

	LoginUser getLoginUserByMobileNumber(String mobileNumber);

	User updateUser(User user);

	List<User> listAllUser();

	List<User> search(String query);

	void remove(String userId);

	void addCredit(String userId, CreditRule creditRule);

	List<Grade> getGrades(String userId);
	//FIXME: urgly interface
	List<BaseGrade> getBaseGradesByEventType(String eventTypeId, int  start, int limit);

	void withdrawCredit(String userId, CreditRecord creditRecord);

	void addCredit(String userId, CreditRecord creditRecord);

	List<User> search(String mobileNum, String wcId, String kidName);

	LoginUser updatePassword(LoginUser existedUser);
	
	UserStat getUserStat(String userId);
	
	void incrLike(String fromUserId, String key);
	
	int getLikeNum(String userId);
	
	boolean existUserInLike(String toUserId, String fromUserId);
	
	List<Map<String, Object>> getUserTracks(String userId, int start, int limit);
	
	void updateLikes(String kidId, String toUserId, int num);
	
	void deleteTempLike(String userId);
	
	Set<String> getTempLikeUsers();
	
	int getTempLikeNum(String key);
}
