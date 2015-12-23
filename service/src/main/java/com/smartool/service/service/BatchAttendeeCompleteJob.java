package com.smartool.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.smartool.common.dto.EventTask;

public class BatchAttendeeCompleteJob implements Job {

	private static Logger logger = Logger.getLogger(BatchAttendeeCompleteJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("batchAttendeeComplete  ....... ");
		try {
			BatchAttendeeComplete batchAttendeeComplete = (BatchAttendeeComplete) context.getScheduler().getContext()
					.get("BatchAttendeeComplete");
			JobDetail jobDetail = context.getJobDetail();
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			List<EventTask> taskList = new ArrayList<>();
			for ( Entry<String, Object> entry : jobDataMap.entrySet()) {
				EventTask eventTask = (EventTask) entry.getValue();
				taskList.add(eventTask);
			}
			batchAttendeeComplete.batch(taskList);
		} catch (SchedulerException e) {
			logger.error("Caught exception when executing BatchAttendeeComplete: ", e);
		}
	}

}
