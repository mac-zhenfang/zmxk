package com.smartool.service.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class EventBackupJob implements Job {

	private static Logger logger = Logger.getLogger(EventBackupJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			EventBackup job = (EventBackup)context.getScheduler().getContext().get("EventBackup");
			job.backup();
		} catch (SchedulerException e) {
			logger.error("Caught exception when executing EventBackupJob: ", e);
		}
	}

}
