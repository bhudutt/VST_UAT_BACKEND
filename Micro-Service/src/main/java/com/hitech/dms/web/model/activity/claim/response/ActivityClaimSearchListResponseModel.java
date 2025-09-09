/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimSearchListResponseModel {
	private BigInteger dealerId;
	private String action;
	private String activityClaimStatus;
	private String activityClaimNo;
	private String activityFromDate;
	private String activityToDate;
	private String activityPlanNo;
	private BigInteger activityClaimHdrId;
	private String activityPlanDate;
	private String dealerCode;
	private String dealerName;
	private String profitCenter;
//	private Integer financialYear;
//	private String financialMonth;
	private BigDecimal totalBudget;
	private BigDecimal totalApprovedBudget;
	private BigDecimal totalReimbursement;
	private BigDecimal claimApprovedAmount; //claim_approved_amount
}
