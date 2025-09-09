package com.hitech.dms.web.model.servicequotation.create.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceQuotationLabourModel {

	private boolean activeStatus;
	private Integer labourGroup;
	 private boolean check;
	private String labourCode;
	private String labourDescription;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private Integer quotationLabourId;
	private Integer quotationId;
	private Integer labourId;
	private BigDecimal standardHrs = BigDecimal.ZERO;
	private BigDecimal rate = BigDecimal.ZERO;
	private BigDecimal basicAmt = BigDecimal.ZERO;
	private Integer discountType;
	private BigDecimal discountRate = BigDecimal.ZERO;
	private BigDecimal discountAmt = BigDecimal.ZERO;
	private BigDecimal chargeAmt = BigDecimal.ZERO;
	private BigDecimal totalAmt = BigDecimal.ZERO;
	private BigDecimal labourAmount = BigDecimal.ZERO;
	private BigDecimal taxAmount;
	private  BigDecimal netAmt=BigDecimal.ZERO;
	private ServiceQuotationModel serviceQuotationModel;
	// private List<ServiceLabourMasterEntity> labourCodeList;
	private Boolean labourMod;
}
