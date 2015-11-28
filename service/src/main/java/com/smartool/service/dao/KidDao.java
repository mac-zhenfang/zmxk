package com.smartool.service.dao;

import java.util.Date;
import java.util.List;

import com.smartool.common.dto.Kid;
import com.smartool.common.dto.Team;

public interface KidDao {
	Kid create(Kid kid);

	Kid update(Kid kid);

	Kid get(String id);

	List<Kid> listAll();

	List<Kid> listByUserId(String userId);

	void remove(String id);

	void removeByUserId(String userId);

	Kid updateAvatarUrl(String kidId, String avatarUrl);
	
	List<String> getDistinctSchoolName(int schoolType);
	
	void setFirstAttendEventTime(String kidId, Date eventTime);
	
	void setTeams(String kidId, String teamId);
	
	void leaveTeams(String kidId, String teamId);
	
	List<String> getTeams(String kidId);
}
