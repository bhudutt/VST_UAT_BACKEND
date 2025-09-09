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
public class ActivityCreditNoteResponseModel {
	private BigInteger srlNo;
//	private BigInteger ActivityClaimInvId; 
	private String activityClaimInvNumber;
	private String claimInvoiceDate;
	private String activityPlanNo;
	private String activityPlanDate;
	private String dealerCode;
	private String dealerName;
	private String profitCenterDesc;
	private Integer month;
	private Integer  financialYear;
	private BigDecimal totalBudget;
	private BigDecimal totalApprovedBudget;
	private BigDecimal totalReimbursementClaim;
	private BigDecimal approvedClaimAmount;
	private String creditNoteNumber;
	private String creditNoteDate;
	private BigDecimal creditNoteAmount;
	private Boolean creditNotePdfLink;
}
