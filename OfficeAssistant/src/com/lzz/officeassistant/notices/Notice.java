package com.lzz.officeassistant.notices;

public class Notice {
	private int id;
	private String title;
	private String content;
	private String time;
	public Notice(int id, String title, String content, String time) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
