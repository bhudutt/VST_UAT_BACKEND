/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimGstInvSearchResponseModel {
	private BigInteger srlNo;
	private String action;
	private BigInteger id; 	//ActivityClaimInvId
	private String activityClaimInvNumber;
	private String finalSubmit;
	private String claimInvoiceDate;
	private String activityPlanNo;
	private String activityPlanDate;
	private String dealerCode;
	private String profitCenterDesc;
	private BigDecimal totalBudget;
	private BigDecimal totalApprovedBudget;
	private BigDecimal totalReimbursementClaim;
	private BigDecimal approvedClaimAmount;
	private BigDecimal gstPer;
	private BigDecimal gstAmount;
	private BigDecimal totalInvoiceAmount;
	private Boolean receivedHardCopy;
	private String plantCode;//added on 12-09-24
	private String plantName;//added on 12-09-24
//	private String creditNoteNumber;
//	private String creditNoteDate;
//	private BigDecimal creditNoteAmount;
//	private Boolean creditNotePdfLink;
}
