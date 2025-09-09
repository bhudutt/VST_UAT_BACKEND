package com.hitech.dms.web.model.activityClaim.approval.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimApproveRequestModel {
	private BigInteger hoUserId;
	private BigInteger activityClaimHdrId;
	private String approvalStatus;
	private String remarks;
	private List<BigDecimal> revisedApprovedAmt;
	private List<BigInteger> actClaimDtlList;
	private Integer approvalLevelSequence;

}
