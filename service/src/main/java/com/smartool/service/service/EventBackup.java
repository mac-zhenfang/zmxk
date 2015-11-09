package com.smartool.service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.Event;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;

public class EventBackup {
	
	private static Logger logger = Logger.getLogger(EventBackup.class);
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private SmartoolServiceConfig config;
	
	@Autowired
	private AttendeeDao attendeeDao;
	
	@Transactional
	public void backup(){
		logger.debug(" delete from ");
		long backupInteval = config.getDefaultEventExpireInteval();
		
		List<Event> events = eventDao.listAllEvent(backupInteval);
		
		List<Event> todoEvents = new ArrayList<>();
		for(Event e : events) {
			if(e.getStatus() !=3){
				todoEvents.add(e);
			}
		}
		if(todoEvents.size() == 0 ) {
			return;
		}
		
		logger.info(" delete events " + todoEvents.size());
		
			
		for(Event e : todoEvents) {
			eventDao.backup(e);
			eventDao.delete(e.getId());
			List<Attendee> attendees = attendeeDao.getAllAttendeeFromEvent(e.getId());
			for(Attendee a : attendees) {
				attendeeDao.backup(a);
			}
			attendeeDao.batchDelete(e.getId());
		}
		
		long backupIntevalHist = config.getDefaultEventExpireIntevalHist();
		eventDao.removeFromHis(backupIntevalHist);
		attendeeDao.removeFromHis(backupIntevalHist);
	}
}
