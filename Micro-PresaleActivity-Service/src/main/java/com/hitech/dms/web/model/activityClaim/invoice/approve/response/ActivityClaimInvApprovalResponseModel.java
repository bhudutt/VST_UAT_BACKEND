/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.approve.response;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvApprovalResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
