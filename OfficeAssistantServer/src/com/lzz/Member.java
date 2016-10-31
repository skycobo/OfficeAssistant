package com.lzz;

public class Member {
	private String teamID;
	private String account;
	private String nickname;
	private String ipAddress;
	private int port=12345;
	public Member(){
		
	}
	public Member(String teamID, String account, String nickname, String ipAddress) {
		super();
		this.teamID = teamID;
		this.account = account;
		this.nickname = nickname;
		this.ipAddress = ipAddress;
	}
	public Member(String teamID, String account, String nickname, String ipAddress , int port) {
		super();
		this.teamID = teamID;
		this.account = account;
		this.nickname = nickname;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	public String getTeamID() {
		return teamID;
	}
	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
