package com.smartool.common.dto;

import java.util.List;

public class User {
	
	String userId;
	
	public String getUserId() {
		return userId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public List<Kid> getKids() {
		return kids;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	String mobileNum;
	
	List<Kid> kids;
	
	String location;
	
	String name;
	
	public User(final String userId, final String mobileNum, final List<Kid> kids, final String localtion, final String name) {
		this.userId = userId;
		this.mobileNum = mobileNum;
		this.kids = kids;
		this.location = localtion;
		this.name = name;
	}
}
