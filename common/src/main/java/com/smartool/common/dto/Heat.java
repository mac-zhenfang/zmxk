package com.smartool.common.dto;

public class Heat {
	
	private int contourId;
	
	private int pathId;

	private String longitude;
	
	private String latitude;
	
	private int index;

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getContourId() {
		return contourId;
	}

	public void setContourId(int contourId) {
		this.contourId = contourId;
	}

	public int getPathId() {
		return pathId;
	}

	public void setPathId(int pathId) {
		this.pathId = pathId;
	}
}
