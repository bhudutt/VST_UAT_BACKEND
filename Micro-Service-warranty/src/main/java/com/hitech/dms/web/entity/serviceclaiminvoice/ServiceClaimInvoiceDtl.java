package com.hitech.dms.web.entity.serviceclaiminvoice;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_SERVICE_CLAIM_INV_DTL")
public class ServiceClaimInvoiceDtl {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	//@ManyToOne
    //@JsonBackReference
    //@JoinColumn(name = "INV_HDR_ID", referencedColumnName = "id")
	@Transient
    private ServiceClaimInvoiceHdr claimInvoiceHdr;
	
	
	@Column(name = "INV_HDR_ID")
	private BigInteger invHdrId;
	
	@Column(name = "RO_ID")
	private BigInteger roId;
	
	@Column(name = "JOBCARD_CAT_ID")
	private BigInteger jobcardCategoryId;
	
	@Column(name = "SERVICE_TYPE_ID")
	private BigInteger serviceTypeId;
	
	@Column(name = "CLAIM_VALUE")
	private BigDecimal claimValue;
	
	@Column(name = "PLANT_CODE")
	private String plantCode;
	
	@Transient
	private String chassisNo;
	
	@Column(name = "CLAIM_DTL_ID")
	private BigInteger claimDtlId;
	
	@Column(name = "cgst_percent")
	private Integer cgstPercent;
	
	@Column(name = "cgst_value")
	private BigDecimal cgstValue;
	
	@Column(name = "sgst_percent")
	private Integer sgstPercent;
	
	@Column(name = "sgst_value")
	private BigDecimal sgstValue;
	
	@Column(name = "igst_percent")
	private Integer igstPercent;
	
	@Column(name = "igst_value")
	private BigDecimal igstValue;
	
	@Column(name = "pdi_id")
	private BigInteger pdiId;
	
	@Column(name = "ins_id")
	private BigInteger insId;
	
							
	
}
