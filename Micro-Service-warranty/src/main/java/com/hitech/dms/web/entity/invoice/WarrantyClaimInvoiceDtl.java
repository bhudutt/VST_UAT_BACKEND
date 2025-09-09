package com.hitech.dms.web.entity.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mahesh.kumar
 */
@Entity
@Getter
@Setter
@Table(name = "WA_CLAIM_INVOICE_DTL")
public class WarrantyClaimInvoiceDtl {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "part_master_id")
	private String partMasterId;
	
	@Column(name = "spmrp")
	private Float spmrp;
	
	@Column(name = "hsn_code")
	private String hsnCode;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "discount_amount")
	private BigDecimal discountAmount;
	
	@Column(name = "discount_percent")
	private BigDecimal discountPercent;
	
	@Column(name = "net_amount")
	private BigDecimal netAmount;
	
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
	
	@Column(name = "sub_total")
	private BigDecimal subTotal;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "grn_done_flag")
	private Boolean grnDoneFlag;
	
	@Column(name="billableType")
	private String billableTypeDesc;
	
	@ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "wcr_invoice_id", referencedColumnName = "id")
    private WarrantyClaimInvoiceHdr warrantyClaimInvoiceHdr;
	

}
