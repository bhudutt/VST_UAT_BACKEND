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
public class ActivityClaimHdrResponseModel {

	private String activityPlanDate;
	private String activityClaimNumber;
	private BigInteger activityPlanHdrId;
	private String activityClaimDate;
	private String profitCenter;
	private Integer pcId;
	private String dealerName;
	private Integer fiannncialYear;
	private Integer fiannncialMonth;
	private String financialMonthDesc;
	private BigDecimal totalClaimAmount;
	private String activityNo;
	private String activityStatus;
	private String seriesName;
	private String segmentName;
	private BigDecimal totalApprovedAmount;
	private String  activityClaimStatus;
	private BigInteger activityClaimHdrId;
	private BigDecimal gstPer;
	private BigDecimal gstAmount;
	private BigDecimal totalInvoiceAmount;
	private String approverFinalRemarks;
	private BigInteger activityClaimInvId;
	private String activityClaimInvNumber;
	
	private String customerInvoiceNumber;
	private String customerInvoiceDate;
	private String finalSubmit;


}
