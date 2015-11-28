package com.smartool.common.dto;

import java.util.List;

public class Kid extends BaseDateTrackingBean {
	private String id;
	private String name;
	private int schoolType;
	private String schoolName;
	private String userId;
	private String avatarUrl;
	private int age;
	private int gender;
	private long firstTimeAttendEvent;
	private String coverVideoLink;
	private int likes;
	
	public Kid() {
	}

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

	public int getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getFirstTimeAttendEvent() {
		return firstTimeAttendEvent;
	}

	public void setFirstTimeAttendEvent(long firstTimeAttendEvent) {
		this.firstTimeAttendEvent = firstTimeAttendEvent;
	}

	public String getCoverVideoLink() {
		return coverVideoLink;
	}

	public void setCoverVideoLink(String coverVideoLink) {
		this.coverVideoLink = coverVideoLink;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
}
