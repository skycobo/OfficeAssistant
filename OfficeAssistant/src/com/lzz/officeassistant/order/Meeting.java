package com.lzz.officeassistant.order;

public class Meeting {
	private String mRGID;
	private String name;
	private String topic;
	private String orderTeam;
	private String startTime;
	private String endTime;
	
	public Meeting(){
		
	}
	public Meeting(String orderTeam, String startTime, String endTime) {
		super();
		this.orderTeam = orderTeam;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Meeting(String mRGID, String name, String topic, String orderTeam, String startTime, String endTime) {
		super();
		this.mRGID = mRGID;
		this.name = name;
		this.topic = topic;
		this.orderTeam = orderTeam;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getmRGID() {
		return mRGID;
	}

	public void setmRGID(String mRGID) {
		this.mRGID = mRGID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getOrderTeam() {
		return orderTeam;
	}

	public void setOrderTeam(String orderTeam) {
		this.orderTeam = orderTeam;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
}
