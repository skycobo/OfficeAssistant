package com.lzz.officeassistant.message;

public class AFJTInfo {
	private String account;
	private String nickname;
	public AFJTInfo(String account, String nickname) {
		super();
		this.account = account;
		this.nickname = nickname;
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
}
