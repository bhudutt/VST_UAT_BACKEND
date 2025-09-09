/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimApproveRequestModel {
	private BigInteger hoUserId;
	private BigInteger activityClaimHdrId;
	private String approvalStatus;
	private String remarks;
	private BigDecimal approvedClaimAmount;
	private List<ActivityClaim> activityList;
}