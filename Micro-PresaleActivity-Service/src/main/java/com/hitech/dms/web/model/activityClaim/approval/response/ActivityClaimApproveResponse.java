package com.hitech.dms.web.model.activityClaim.approval.response;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimApproveResponse {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;

}
