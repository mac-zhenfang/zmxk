package com.smartool.service.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerService {
	

	private final static String EVENT_COMPLETE_TIMER_KEY = "EventCompleteExecutor";
	
	@Autowired
	private Scheduler scheduler;
	
	public void iocInit() throws SchedulerException {

		if (scheduler.getTrigger(TriggerKey.triggerKey("EventStartNotificationJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventStartNotificationJob.class)
					.withIdentity(JobKey.jobKey("EventStartNotificationJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(TriggerKey.triggerKey("EventStartNotificationJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}

		if (scheduler.getTrigger(TriggerKey.triggerKey("EventBackupJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventBackupJob.class)
					.withIdentity(JobKey.jobKey("EventBackupJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("EventBackupJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}

		if (scheduler.getTrigger(TriggerKey.triggerKey(EVENT_COMPLETE_TIMER_KEY)) == null) {
			JobDetail jobDetail = JobBuilder.newJob(EventBackupJob.class)
					.withIdentity(JobKey.jobKey(EVENT_COMPLETE_TIMER_KEY)).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(EVENT_COMPLETE_TIMER_KEY))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}
		
		//credit

		//FIXME put cron into properties
		if (scheduler.getTrigger(TriggerKey.triggerKey("CreditGeneratorTrigger")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(CreditGeneratingJob.class)
					.withIdentity(JobKey.jobKey("CreditGeneratorJob")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("CreditGeneratorTrigger"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}
		
		
		if (scheduler.getTrigger(TriggerKey.triggerKey("LikesAuditJobTimer")) == null) {
			JobDetail jobDetail = JobBuilder.newJob(LikesAuditJob.class)
					.withIdentity(JobKey.jobKey("LikesAuditJobTimer")).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey("LikesAuditJobTimer"))
					.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
			scheduler.scheduleJob(jobDetail, trigger);
		}
	

	}
}
