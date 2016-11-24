package com.lzz.officeassistant.review;

import java.io.Serializable;

public class ReviewInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String reviewID;
	private String reviewTitle;
	private String reviewType;
	private String reviewContent;
	private String commitTime;
	private String requester;
	private String handler;
	private String status;
	private String reviewIdea;
	private String responseTime;
	public ReviewInfo(){
	}
	public ReviewInfo(String reviewID, String reviewTitle, String reviewType, String reviewContent, String commitTime, String requester,
			String handler, String status, String reviewIdea, String responseTime) {
		super();
		this.reviewID = reviewID;
		this.reviewTitle = reviewTitle;
		this.reviewType = reviewType;
		this.reviewContent = reviewContent;
		this.commitTime = commitTime;
		this.requester = requester;
		this.handler = handler;
		this.status = status;
		this.reviewIdea = reviewIdea;
		this.responseTime = responseTime;
	}
	public String getReviewID() {
		return reviewID;
	}
	public void setReviewID(String reviewID) {
		this.reviewID = reviewID;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewType() {
		return reviewType;
	}
	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReviewIdea() {
		return reviewIdea;
	}
	public void setReviewIdea(String reviewIdea) {
		this.reviewIdea = reviewIdea;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

}
