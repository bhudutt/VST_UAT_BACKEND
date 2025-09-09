package com.hitech.dms.web.entity.quotation.create.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "SV_QUOTATION_PRT")
@Data
public class ServiceQuotationPartEntity {

	@Id
	@Column(name = "Quotation_Part_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer quotationPartId;
	
	@Column(name = "PartBranch_Id")
	private Integer partBranchId;
	
	@Column(name = "Part_Id")
	private Integer partId;

	@Column(name = "QtyReq")
	private BigDecimal qtyReq;

	@Column(name = "Rate")
	private BigDecimal rate;

	@Column(name = "BasicAmt")
	private BigDecimal basicAmt;

	@Column(name = "DiscountType")
	private Integer discountType;

	@Column(name = "DiscountRate")
	private BigDecimal discountRate;

	@Column(name = "DiscountAmt")
	private BigDecimal discountAmt;

	@Column(name = "ChargeAmt")
	private BigDecimal taxAmt;

	@Column(name = "TotalAmt")
	private BigDecimal totalAmt;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	
	@JoinColumn(name = "Quotation_Id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ServiceQuotationEntity serviceQuotationEntity;

	private transient Boolean deletablePartId;
	
	@Transient
	private Boolean check;
	
}
