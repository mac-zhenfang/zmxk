package com.smartool.service.service;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.smartool.common.dto.Attendee;
import com.smartool.common.dto.Event;
import com.smartool.common.dto.User;
import com.smartool.service.config.SmartoolServiceConfig;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.UserDao;
import com.smartool.service.sms.SmsClient;

public class EventStartNotification {
	
private static Logger logger = Logger.getLogger(EventStartNotificationJob.class);
	
	@Autowired 
	EventDao eventDao;
	
	@Autowired
	AttendeeDao attendeeDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private SmsClient smsClient;

	@Autowired
	private VelocityEngine velocityEngine;
	
	
	@Autowired
	SmartoolServiceConfig config;
	
	private final static int NOT_START_EVENT = 0;
	
	public void notice() {

		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", NOT_START_EVENT);
		List<Event> events = eventDao.search(param);
		logger.debug("Notice   ....... get events " + events);
		for(Event event : events) {
			Date startDate = event.getEventTime();
			long startDateTime = startDate.getTime();
			logger.debug("time to fire " + (startDateTime - System.currentTimeMillis()));
			logger.debug("inteval    ......." + config.getEventNofityTime());

			if(startDateTime - System.currentTimeMillis() <= config.getEventNofityTime() && startDateTime - System.currentTimeMillis() > 0) {
				logger.info("Get event need notify   ......." + event.getName());

				List<Attendee> attendees = event.getAttendees();
				
				logger.info("Get event need notify  attendees  ......." + attendees);
				for(Attendee attendee : attendees) {
					if(!Strings.isNullOrEmpty(attendee.getId())) {
						logger.info("get attendee times  ....... " + attendee.getAttendeeNotifyTimes());
						
						if(attendee.getAttendeeNotifyTimes() < config.getNeedNotifyTimes()) {
							User user = userDao.getUserById(attendee.getUserId());
							logger.info("notify user ....." + attendee.getUserId() + " " + attendee.getKidName());
							
							Template t = velocityEngine.getTemplate( "templates/notify.vm", "UTF-8" );
							VelocityContext context = new VelocityContext();
							context.put("kidName",attendee.getKidName());
							context.put("eventSerieName", event.getEventType());
							context.put("eventNotifyTime", (config.getEventNofityTime()/60000 -2));

							StringWriter writer = new StringWriter();
						    t.merge( context, writer );
						    String msg =  writer.toString();
						    
						    //FIXME Retry Template
						    for (int i = 0; i < 3; i++) {
						    	if(smsClient.send(user.getMobileNum(), msg)) {
						    		attendee.setAttendeeNotifyTimes(attendee.getAttendeeNotifyTimes()+1);;
							    	attendeeDao.updateNotifyTimes(attendee);
									break;
								}
							}
						}
					}
				}
			}
		}
	
	}
}
