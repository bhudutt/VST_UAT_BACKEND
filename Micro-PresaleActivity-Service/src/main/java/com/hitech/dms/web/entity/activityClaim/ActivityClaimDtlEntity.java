package com.hitech.dms.web.entity.activityClaim;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Table(name = "SA_ACT_CLAIM_DTL")
@Entity
@Data
public class ActivityClaimDtlEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7305620195167055242L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_claim_dtl_id")
	private BigInteger activityClaimDtlId;
	
	@JoinColumn(name = "activity_claim_hdr_id")
    @ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private ActivityClaimHdrEntity acHdr;
	
	
	@JsonProperty(value = "activityActualHdrId", required = true)
	@NotNull(message = "Activity Actual Hdr is Required")
	@Column(name = "activity_actual_hdr_id")
	private BigInteger activityActualHdrId;
	
	@JsonProperty(value = "totalEnquiries", required = true)
	@NotNull(message = "Total Enquiries is Required")
	@Column(name = "total_enquiries")
	private Integer totalEnquiries;
	
	@JsonProperty(value = "totalBudget", required = true)
	@NotNull(message = "Total Budget is Required")
	@Column(name = "total_budget")
	private BigDecimal totalBudget;
	
	@JsonProperty(value = "approvedBudget", required = true)
	@NotNull(message = "Approved Budget is Required")
	@Column(name = "approved_budget")
	private BigDecimal approvedBudget;
	
	
	@JsonProperty(value = "reimbursementClaim", required = true)
	@NotNull(message = "Reimbursement Claim is Required")
	@Column(name = "reimbursement_claim")
	private BigDecimal reimbursementClaim;
	
	
	@JsonProperty(value = "vendorInvoiceNo", required = true)
	@NotNull(message = "Vendor Invoice No is Required")
	@Column(name = "vendor_invoice_no")
	private String vendorInvoiceNo;
	
	@JsonProperty(value = "vendorInvoiceAmount", required = true)
	@NotNull(message = "Vendor Invoice Amount is Required")
	@Column(name = "vendor_invoice_amount")
	private BigDecimal vendorInvoiceAmount;
	
	@Column(name = "vendor_invoice_file")
	private String vendorInvoiceFile;
	
	@Column(name = "actiity_photo_file")
	private String actiityPhotoFile;
	
	@Column(name = "approved_claim_amount")
	private BigDecimal approvedClaimAmount;
	
	@Column(name = "approver_remarks")
	private String approverRemarks;
	
	@Column(name = "GL_CODE")
	private String glCode;
	
	@Column(name = "HSN_Code")
	private String hsnCode;
	

}
