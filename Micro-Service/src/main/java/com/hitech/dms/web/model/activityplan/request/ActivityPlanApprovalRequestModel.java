package com.hitech.dms.web.model.activityplan.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ActivityPlanApprovalRequestModel {

	private Integer activityId;
	private String approvalStatus;
	private String remarks;
}
