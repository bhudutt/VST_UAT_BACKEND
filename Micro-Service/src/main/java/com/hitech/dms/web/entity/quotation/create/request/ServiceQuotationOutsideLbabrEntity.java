package com.hitech.dms.web.entity.quotation.create.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import lombok.Data;

@Table(name = "SV_QUOTATION_OUTSIDE_LBR")
@Entity
@Data
public class ServiceQuotationOutsideLbabrEntity {



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Quotation_Outside_Labour_Id")
	private Integer quotationOutsideLabourId;
	
	@Column(name = "Labour_Id")
	private Integer labourId;

	@Column(name = "StandardHrs")
	private BigDecimal standardHrs;

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
	private BigDecimal taxAmount;

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
	
	@ManyToOne
	@JoinColumn(name = "Quotation_Id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private ServiceQuotationEntity serviceQuotationEntity;
	
	@Transient
	private Integer labourGroup;
	@Transient
	private String labourCode;
	@Transient
	private String labourDescription;
	@Transient
	private Boolean check;

}
