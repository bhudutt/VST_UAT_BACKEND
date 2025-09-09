package com.hitech.dms.web.model.activityplan.response;

import lombok.Data;

@Data
public class ActivityPlanApprovalResponse {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}

