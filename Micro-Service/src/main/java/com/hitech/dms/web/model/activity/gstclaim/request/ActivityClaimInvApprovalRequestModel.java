/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimInvApprovalRequestModel {
	private BigInteger activityClaimInvHdrId;
	private String approvalStatus;
	private String remarks;
}
