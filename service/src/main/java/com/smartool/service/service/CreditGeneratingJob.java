package com.smartool.service.service;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

@DisallowConcurrentExecution
public class CreditGeneratingJob implements Job {
	private static Logger logger = Logger.getLogger(CreditGeneratingJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("CreditGeneratingJob get scheduled.");
		try {
			CreditGenerator creditGenerator = (CreditGenerator) context.getScheduler().getContext()
					.get("CreditGenerator");
			creditGenerator.generateCredit();
		} catch (SchedulerException e) {
			logger.error("Caught exception when executing CreditGeneratingJob: ", e);
		}
	}

}
