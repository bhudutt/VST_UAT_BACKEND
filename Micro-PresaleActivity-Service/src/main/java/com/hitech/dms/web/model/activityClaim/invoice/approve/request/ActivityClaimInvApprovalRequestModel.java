/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.approve.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvApprovalRequestModel {
	private BigInteger activityClaimInvHdrId;
	private String approvalStatus;
	private String remarks;
}
