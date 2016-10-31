package com.lzz.officeassistant.signin;

public class SignInInfo {
	private String nickname;
	private String position;
	private String time;
	public SignInInfo(){
		
	}
	public SignInInfo(String nickname, String position, String time) {
		super();
		this.nickname = nickname;
		this.position = position;
		this.time = time;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
