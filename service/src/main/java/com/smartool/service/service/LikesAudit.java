package com.smartool.service.service;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Splitter;
import com.smartool.service.dao.UserDao;

public class LikesAudit {
	
	private static Logger logger = Logger.getLogger(LikesAudit.class);
	
	private static Splitter splitter = Splitter.on("|");
	
	@Autowired
	UserDao userDao;
	
	public void audit(){
		Set<String> usersInTemp = userDao.getTempLikeUsers();
		logger.debug(" need to audit " + usersInTemp.size() );
		for(String key : usersInTemp) {
			List<String> pair = splitter.splitToList(key);
			int tempLikeNum = userDao.getTempLikeNum(key);
			userDao.updateLikes(pair.get(0), pair.get(1), tempLikeNum);
			userDao.deleteTempLike(key);
		}
	}
	
}
