package com.hitech.dms.web.entity.serviceclaiminvoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_SERVICE_CLAIM_INV_HDR")
public class ServiceClaimInvoiceHdr {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name = "CLAIM_INVOICE_NO")
	private String claimInvoiceNo;
	
	@Column(name = "CLAIM_HDR_ID")
	private BigInteger claimHdrId;
	
	@Column(name = "CLAIM_INVOICE_DATE")
	private Date claimInvoiceDate;
	
	@Column(name = "BASE_PRICE")
	private BigDecimal basePrice;
	
	@Column(name = "GST_AMOUNT")
	private BigDecimal gstAmount;
	
	@Column(name = "INV_AMOUNT")
	private BigDecimal invoiceAmount;
	
	@Column(name = "CR_NO")
	private String creditNo;
	
	@Column(name = "CR_AMOUNT")
	private BigDecimal creditAmount;
	
	@Column(name = "CR_DATE")
	private Date creditDate;
	
	//@OneToMany(mappedBy = "claimInvoiceHdr",cascade = CascadeType.ALL)
    //@JsonManagedReference
	@Transient
    private List<ServiceClaimInvoiceDtl> claimInvoiceDtls;
	
	@Column(name = "CREATEDBY")
	private BigInteger createdBy;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate;
	
	@Column(name = "MODIFIEDBY")
	private BigInteger modifiedBy;
	
	@Column(name = "MODIFIEDDATE")
	private Date modifiedDate;
	
	@Column(name = "PLANT_CODE")
	private String plantCode;
	
	@Column(name = "customer_invoice_date")
	private String customerInvoiceDate;
	
	@Column(name = "customer_invoice_no")
	private String customerInvoiceNo;
	
	@Transient
	private String finalSubmitFlag;
}
