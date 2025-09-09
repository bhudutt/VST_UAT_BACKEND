package com.hitech.dms.web.model.activityClaim.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimSearchListResponseModel {

	private BigInteger dealerId;
	private String action;
	private String activityClaimStatus;
	private String activityClaimNo;
//	private String activityFromDate;
//	private String activityToDate;
	private String activityPlanNo;
	private BigInteger id;
	private String activityPlanDate;
	private String dealerCode;
	private String dealerName;
	private String profitCenter;
	private Integer financialYear;
	private String financialMonth;
	private BigDecimal totalBudget;
	private BigDecimal totalApprovedBudget;
	private BigDecimal totalReimbursement;
	private BigDecimal claimApprovedAmount; //claim_approved_amount
	private String remarks;
	private String levelOneApprovedDate;
	private String levelTwoApprovedDate;
	private String levelThreeApprovedDate;

}
