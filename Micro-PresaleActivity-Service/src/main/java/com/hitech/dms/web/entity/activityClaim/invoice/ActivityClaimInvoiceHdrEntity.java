/**
 * 
 */
package com.hitech.dms.web.entity.activityClaim.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_CLAIM_INVOICE_HDR")
@Entity
@Data
public class ActivityClaimInvoiceHdrEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2573485688304315282L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_claim_inv_hdr_id")
	private BigInteger activityClaimInvHdrId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;

	@Column(name = "Invoice_type")
	private String invoiceType;

	@Column(name = "activity_claim_hdr_id")
	private BigInteger activityClaimHdrId;

	@Column(name = "claim_invoice_no")
	private String claimInvoiceNo;

	@Column(name = "claim_invoice_date")
	private Date claimInvoiceDate;

	@Column(name = "claim_invoice_status")
	private String claimInvoiceStatus;

	@Column(name = "total_invoice_amount")
	private BigDecimal totalInvoiceAmnt;
	
	@Column(name = "Gst_Per")
	private BigDecimal gstPer;
	
	@Column(name = "Gst_Amnt")
	private BigDecimal gstAmnt;
	
	
	@Column(name="location_plant_code")
	private String location;

	@Column(name = "final_approver_remarks")
	private String finalApproverRemarks;

	@Column(name = "credit_note_no")
	private String creditNoteNo;

	@Column(name = "claim_note_date")
	private Date creditNoteDate;

	@Column(name = "credit_note_amount")
	private BigDecimal creditNoteAmnt;

	@Column(name = "claim_note_remarks")
	private String creditNoteRemarks;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;
	@Column(name = "created_on", updatable = false)
	private Date createdDate;
	@Column(name = "modified_by")
	private BigInteger modifiedBy;
	@Column(name = "modified_on")
	private Date modifiedDate;
	
	@Column(name="customer_invoice_no") 
	private String customerInvoiceNo;
	
	@Column(name="customer_invoice_date") 
	private String customerInvoiceDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "activityClaimInvoice", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<ActivityClaimInvoiceDtlEntity> activityClaimInvDtlList;
}
