/**
 * 
 */
package com.hitech.dms.web.model.activityClaim.invoice.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonIgnoreProperties({"gstAmount","totalInvoiceAmount"})
public class ActivityClaimInvSearchResponseModel {
	private BigInteger srlNo;
	private String action;
	private String finalSubmit;
	private BigInteger id; 	//ActivityClaimInvId
	private String activityClaimInvNumber;
	private String status;
	private String claimInvoiceDate;
	private String activityPlanNo;
	private String activityPlanDate;
	private String dealerCode;
	private String profitCenterDesc;
	private Integer financialYear;
	private String financialMonth;
	private BigDecimal totalBudget;
	private BigDecimal totalApprovedBudget;
	private BigDecimal totalReimbursementClaim;
	private BigDecimal approvedClaimAmount;
	private BigDecimal gstPer;
	private BigDecimal gstAmount;
	private BigDecimal totalInvoiceAmount;
	private Boolean receivedHardCopy;
	private String creditNoteNumber;
	private String creditNoteDate;
	private BigDecimal creditNoteAmount;
	private Boolean creditNotePdfLink;
	private String remarks;
	private String customerInvoiceNo;
	private String customerInvoiceDate;
	private String approvedDate;
}
