package com.lzz.officeassistant.review;

public class ReviewInfo {
	private String requester;
	private String handler;
	private String reviewType;
	private String reviewTitle;
	private String reviewContent;
	private String requestTime;
	private String responseTime;
	private String status;
	private String reviewIdea;
	public ReviewInfo(){
	}
	public ReviewInfo(String requester, String handler, String reviewType, String reviewTitle, String reviewContent,
			String requestTime, String responseTime, String status, String reviewIdea) {
		super();
		this.requester = requester;
		this.handler = handler;
		this.reviewType = reviewType;
		this.reviewTitle = reviewTitle;
		this.reviewContent = reviewContent;
		this.requestTime = requestTime;
		this.responseTime = responseTime;
		this.status = status;
		this.reviewIdea = reviewIdea;
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
	public String getReviewType() {
		return reviewType;
	}
	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
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
	
}
