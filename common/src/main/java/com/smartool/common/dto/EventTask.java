package com.smartool.common.dto;

import java.io.Serializable;

public class EventTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1574709427340873238L;

	private String eventId;

	private int status;

	private String dataUrl;

	private long createdTime;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
