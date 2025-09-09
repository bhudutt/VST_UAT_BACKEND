/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityClaimInvHdrResponseModel {
	private BigInteger dealerId;
	private BigInteger activityClaimInvId;
	private String activityClaimInvNumber;
	private BigInteger activityClaimHdrId;
	private String activityClaimNumber;
	private BigInteger activityPlanHdrId;
	private String activityClaimDate;
	private String activityPlanDate;
	private String profitCenter;
	private Integer pcId;
	private String dealerName;
	private Integer fiannncialYear;
	private Integer fiannncialMonth;
	private String financialMonthDesc;
	private BigDecimal totalInvoiceAmnt;
	private BigDecimal totalClaimAmount;
	private String activityNo;
	private String activityStatus;
	private String seriesName;
	private String segmentName;
	private BigDecimal totalApprovedAmount;
	private String  activityClaimStatus;
	private BigDecimal gstPer;
	private BigDecimal gstAmount;
	private BigDecimal totalInvoiceAmount;
	private String customerInvoiceNumber;
	private String customerInvoiceDate;
	private String finalSubmit;
	private List<ActivityClaimInvDtlResponseModel> activityClaimInvDtlList;	
}
