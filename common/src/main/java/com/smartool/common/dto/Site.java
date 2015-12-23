package com.smartool.common.dto;

public class Site extends BaseDateTrackingBean {

	private String id;
	
	private String name;
	
	private String location;
	
	private int siteNum;
	
	private int siteLevel;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getSiteNum() {
		return siteNum;
	}

	public void setSiteNum(int siteNum) {
		this.siteNum = siteNum;
	}

	public int getSiteLevel() {
		return siteLevel;
	}

	public void setSiteLevel(int siteLevel) {
		this.siteLevel = siteLevel;
	}
}
