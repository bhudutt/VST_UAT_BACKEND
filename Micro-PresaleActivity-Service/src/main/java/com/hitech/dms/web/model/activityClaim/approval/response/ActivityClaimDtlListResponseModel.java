package com.hitech.dms.web.model.activityClaim.approval.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimDtlListResponseModel {
	
	//private BigInteger activityActualHdrId;
	private BigInteger activityActualHdrId;
	private BigInteger activityClaimDtlId;
	private String activityActualNo;
	private String activityName;
	private BigDecimal revisedApprovedBudget;
	private String activityLocation;
	private Integer totalEnquiries;
	private BigDecimal totalBudget;
	private BigDecimal approvedBudget;
	private BigDecimal reimbursementClaim;
	private String vendorInvoiceNo;
	private BigDecimal vendorInvoiceAmount;
	private BigDecimal approvedClaimAmount;
	private String vendorInvoiceFile;
	private String actiityPhotoFile;
	private String approverRemarks;
	private String glCode;
	private String hsnCode;
}
