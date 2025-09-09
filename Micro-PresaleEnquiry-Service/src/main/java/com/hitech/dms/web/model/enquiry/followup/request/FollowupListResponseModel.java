package com.hitech.dms.web.model.enquiry.followup.request;

public class FollowupListResponseModel {

	private Integer followupId;
	private String followupActionName;
	private String followupStatus;
	private Integer createdby;
	
	public Integer getFollowupId() {
		return followupId;
	}
	public void setFollowupId(Integer followupId) {
		this.followupId = followupId;
	}
	public String getFollowupActionName() {
		return followupActionName;
	}
	public void setFollowupActionName(String followupActionName) {
		this.followupActionName = followupActionName;
	}
	public String getFollowupStatus() {
		return followupStatus;
	}
	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}	
	
}
