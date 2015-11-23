package com.smartool.service.service;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartool.service.dao.UserDao;

public class LikesAudit {
	
	private static Logger logger = Logger.getLogger(LikesAudit.class);
	
	@Autowired
	UserDao userDao;
	
	public void audit(){
		Set<String> usersInTemp = userDao.getTempLikeUsers();
		logger.info(" need to audit " + usersInTemp.size() );
		for(String userId : usersInTemp) {
			int tempLikeNum = userDao.getTempLikeNum(userId);
			userDao.updateLikes(userId, tempLikeNum);
			userDao.deleteTempLike(userId);
		}
	}
	
}
