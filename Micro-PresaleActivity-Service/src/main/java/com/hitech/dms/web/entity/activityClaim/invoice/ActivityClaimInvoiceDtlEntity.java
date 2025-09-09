/**
 * 
 */
package com.hitech.dms.web.entity.activityClaim.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_CLAIM_INVOICE_DTL")
@Entity
@Data
public class ActivityClaimInvoiceDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -34648197702063398L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_claim_inv_dtl_id")
	private BigInteger activityClaimInvDtlId;
	
	@JoinColumn(name="activity_claim_inv_hdr_id")
    @ManyToOne(fetch = FetchType.LAZY)
	private ActivityClaimInvoiceHdrEntity activityClaimInvoice;
	
	@Column(name = "item_code")
	private String itemCode;
	
	@Column(name = "item_details")
	private String itemDetails;
	
	@Column(name = "hsn_code")
	private String hsnCode;
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@JsonProperty(value ="approvedAmount")
	@Column(name = "amount")
	private BigDecimal approvedAmount;
	
	@Column(name = "discount_percent")
	private BigDecimal discountPercent;
	
	@Column(name = "discount_amount")
	private BigDecimal discountAmount;
	
	@Column(name = "net_amount")
	private BigDecimal netAmnt;
	
	@Column(name = "cgst_amount")
	private BigDecimal cgstAmount;
	
	@Column(name = "cgst_percent")
	private BigDecimal cgstPercent;
	
	@Column(name = "sgst_amount")
	private BigDecimal sgstAmount;
	
	@Column(name = "sgst_percent")
	private BigDecimal sgstPercent;
	
	@Column(name = "igst_amount")
	private BigDecimal igstAmount;
	
	@Column(name = "igst_percent")
	private BigDecimal igstPercent;
	
	@Column(name = "gst_amount")
	private BigDecimal gstAmount;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "gl_code")
	private String glCode;
	
	@Column(name = "activity_actual_from_date")
	private Date activityActualFromDate;
	
	@Column(name = "activity_actual_to_date")
	private Date activityActualToDate;
	
	
	
	
}
