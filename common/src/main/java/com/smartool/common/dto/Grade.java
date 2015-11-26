package com.smartool.common.dto;

import java.util.List;

public class Grade extends BaseGrade {

	private int credit;
	
	private int likes;
	
	private String videoLink;
	
	private List<Trophy> trophyList;

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public List<Trophy> getTrophyList() {
		return trophyList;
	}

	public void setTrophyList(List<Trophy> trophyList) {
		this.trophyList = trophyList;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

}
