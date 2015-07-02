package com.smartool.common.dto;

import java.util.List;

public class User {
	String id;
	String name;
	String mobileNum;
	String wcId;
	String location;
	List<Kid> kids;

	public User() {
	}

	public String getWcId() {
		return wcId;
	}

	public void setWcId(String wcId) {
		this.wcId = wcId;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public void setKids(List<Kid> kids) {
		this.kids = kids;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
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
}
