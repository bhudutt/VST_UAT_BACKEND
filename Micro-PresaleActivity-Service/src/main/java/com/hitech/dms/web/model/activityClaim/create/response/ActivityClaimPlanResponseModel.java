package com.hitech.dms.web.model.activityClaim.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class ActivityClaimPlanResponseModel {
	
	private BigInteger activityActualHdrId;
	private String activityActualNo;
//	private String activityActualFromDate;
//	private String activityActualToDate;
	private String activityName;
	private String activityLocation;
	private Integer totalEnquiries;
	private BigDecimal totalBudget;
	private BigDecimal approvedBudget;
	private BigDecimal reimbursementClaim;
	private String vendorInvoiceNo;
	private BigDecimal vendorInvoiceAmount;
	private String approverRemarks;
	private String glCode;
	private String hsnCode;
	
}
