package com.smartool.common.dto;

public class Attendee extends BaseDateTrackingBean {

	private String id;

	private String kidName;

	private String kidId;

	private String userId;

	private String eventId;
	
	private String avatarUrl;

	private String tagId;

	private String tag;

	private int score;

	private int rank;

	private Integer seq;
	
	private String teamName;
	
	private String teamId;
	
	private int schoolType;
	
	private String schoolName;
	
	private String nextRoundId;

	private int status; // 0 (prepared when create event) / 1 (enrolled) / 2
						// (complete event)
	private Integer attendeeNotifyTimes;
	
	private String roundId;
	
	private String roundShortName;
	
	private String roundLevelName;
	
	private int roundLevel;
	
	private String videoLink;
	
	public Attendee() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getKidId() {
		return kidId;
	}

	public void setKidId(String kidId) {
		this.kidId = kidId;
	}

	public String getKidName() {
		return kidName;
	}

	public void setKidName(String kidName) {
		this.kidName = kidName;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getAttendeeNotifyTimes() {
		return attendeeNotifyTimes;
	}

	public void setAttendeeNotifyTimes(int attendeeNotifyTimes) {
		this.attendeeNotifyTimes = attendeeNotifyTimes;
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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public String getRoundShortName() {
		return roundShortName;
	}

	public void setRoundShortName(String roundShortName) {
		this.roundShortName = roundShortName;
	}

	public String getRoundLevelName() {
		return roundLevelName;
	}

	public void setRoundLevelName(String roundLevelName) {
		this.roundLevelName = roundLevelName;
	}

	public int getRoundLevel() {
		return roundLevel;
	}

	public void setRoundLevel(int roundLevel) {
		this.roundLevel = roundLevel;
	}

	public String getNextRoundId() {
		return nextRoundId;
	}

	public void setNextRoundId(String nextRoundId) {
		this.nextRoundId = nextRoundId;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

}