package com.smartool.service.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class EventStartNotificationJob implements Job {
	private static Logger logger = Logger.getLogger(EventStartNotificationJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		logger.info("EventStartNotificationJob  ....... ");
//		try {
//			EventStartNotification eventStartNotification = (EventStartNotification) context.getScheduler().getContext()
//					.get("EventStartNotification");
//			eventStartNotification.notice();
//		} catch (SchedulerException e) {
//			logger.error("Caught exception when executing EventStartNotification: ", e);
//		}
	}
}
