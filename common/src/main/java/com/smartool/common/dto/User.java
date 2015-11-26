package com.smartool.common.dto;

import java.util.List;

public class User extends BaseDateTrackingBean {
	private String id;
	private String name;
	
	private String mobileNum;
	// private String wcId;
	private String location;
	private String roleId;
	private String siteId;
	private List<Kid> kids;
	private int status;
	private int credit;
	private int goldenMedal;
	private int silverMedal;
	private int bronzeMedal;
	private int idp;
	private int likes;
	private int maxTeamMemberSize;

	public int getGoldenMedal() {
		return goldenMedal;
	}

	public void setGoldenMedal(int goldenMedal) {
		this.goldenMedal = goldenMedal;
	}

	public int getSilverMedal() {
		return silverMedal;
	}

	public void setSilverMedal(int silverMedal) {
		this.silverMedal = silverMedal;
	}

	public int getBronzeMedal() {
		return bronzeMedal;
	}

	public void setBronzeMedal(int bronzeMedal) {
		this.bronzeMedal = bronzeMedal;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public User() {
	}

	// public String getWcId() {
	// return wcId;
	// }
	//
	// public void setWcId(String wcId) {
	// this.wcId = wcId;
	// }

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


	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public int getIdp() {
		return idp;
	}

	public void setIdp(int idp) {
		this.idp = idp;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getMaxTeamMemberSize() {
		return maxTeamMemberSize;
	}

	public void setMaxTeamMemberSize(int maxTeamMemberSize) {
		this.maxTeamMemberSize = maxTeamMemberSize;
	}
}
