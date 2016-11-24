package com.lzz.officeassistant.order;

import java.io.Serializable;

public class MeetingRoom implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String mRGID;
	private String location;
	private String floor;
	private String accommodate;
	private String wifi;
	private String projector;
	private String othersSupport;

	
	public MeetingRoom(){
		
	}

	public MeetingRoom(String name, String mRGID, String location, String floor, String accommodate, String wifi,
			String projector, String othersSupport) {
		super();
		this.name = name;
		this.mRGID = mRGID;
		this.location = location;
		this.floor = floor;
		this.accommodate = accommodate;
		this.wifi = wifi;
		this.projector = projector;
		this.othersSupport = othersSupport;

	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getmRGID() {
		return mRGID;
	}

	public void setmRGID(String mRGID) {
		this.mRGID = mRGID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getAccommodate() {
		return accommodate;
	}

	public void setAccommodate(String accommodate) {
		this.accommodate = accommodate;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getProjector() {
		return projector;
	}

	public void setProjector(String projector) {
		this.projector = projector;
	}

	public String getOthersSupport() {
		return othersSupport;
	}

	public void setOthersSupport(String othersSupport) {
		this.othersSupport = othersSupport;
	}
}
