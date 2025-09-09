/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.request;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimInvDtlRequestModel {
	private int activity_claim_hdr_id;
	private BigDecimal totalAmount;
	private String ActivityNumber;
	private int ActivityId;
	private String ActivitySourceName;
	private BigDecimal reimbursement_claim;
	private String vendor_invoice_no;
	private String actualActivityFromDate;
	private String actualActivityToDate;
	private BigDecimal vendor_invoice_amount;
	private BigDecimal approveBudget;

}
