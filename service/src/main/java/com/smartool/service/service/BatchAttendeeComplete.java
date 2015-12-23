package com.smartool.service.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.smartool.common.dto.EventTask;
import com.smartool.service.config.SmartoolServiceConfig;

public class BatchAttendeeComplete {

	@Autowired
	EventService eventService;

	@Autowired
	SmartoolServiceConfig config;

	public void batch(List<EventTask> eventTaskList) {
		ExecutorService executor = Executors.newFixedThreadPool(config.getEventCompleteHandleExecutorNum());

		for (final EventTask eventTask : eventTaskList) {
			if (eventTask.getStatus() == 0 && !Strings.isNullOrEmpty(eventTask.getDataUrl())
					&& !Strings.isNullOrEmpty(eventTask.getEventId())) {
				executor.submit(new Runnable(){
					
					@Override
					public void run() {
						
					}});
			}
		}
	}
}
