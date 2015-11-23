package com.smartool.service.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class LikesAuditJob implements Job {

	private static Logger logger = Logger.getLogger(LikesAuditJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			LikesAudit job = (LikesAudit) context.getScheduler().getContext().get("LikesAudit");
			job.audit();
		} catch (SchedulerException e) {
			logger.error("Caught exception when executing LikesAudit: ", e);
		}
	}

}
