package com.smartool.common.dto;

import java.util.List;

public class UserGrade implements Cover {
	
	private String id;
	private int credit;
	private int totalUserCredit;
	private String siteId;
	private String siteName;
	private String eventTypeId;
	private String name;
	private String centerLogoIcon;
	private int fatestTime;
	private int totalAttendTimes;
	private List<Achievement> achievementList;
	
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

	public String getCenterLogoIcon() {
		return centerLogoIcon;
	}

	public void setCenterLogoIcon(String centerLogoIcon) {
		this.centerLogoIcon = centerLogoIcon;
	}

	public int getFatestTime() {
		return fatestTime;
	}

	public void setFatestTime(int fatestTime) {
		this.fatestTime = fatestTime;
	}

	public int getTotalAttendTimes() {
		return totalAttendTimes;
	}

	public void setTotalAttendTimes(int totalAttendTimes) {
		this.totalAttendTimes = totalAttendTimes;
	}

	public List<Achievement> getAchievementList() {
		return achievementList;
	}

	public void setAchievementList(List<Achievement> trophyList) {
		this.achievementList = trophyList;
	}

	public Kid getKid() {
		return kid;
	}

	public void setKid(Kid kid) {
		this.kid = kid;
	}


	private Kid kid;
	
	@Override
	public Type getType() {
		return Type.grade;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getTotalUserCredit() {
		return totalUserCredit;
	}

	public void setTotalUserCredit(int totalUserCredit) {
		this.totalUserCredit = totalUserCredit;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
}
