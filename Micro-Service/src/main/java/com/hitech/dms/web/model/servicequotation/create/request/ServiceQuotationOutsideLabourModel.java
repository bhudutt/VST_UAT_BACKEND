package com.hitech.dms.web.model.servicequotation.create.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceQuotationOutsideLabourModel {

	private boolean activeStatus;
	private String labourCode;
	private String labourDescription;
	
	private Integer quotationId;
	private Integer quotationOutsideLabourId;
	private Integer labourId;
	private BigDecimal standardHrs = BigDecimal.ZERO;
	private BigDecimal rate = BigDecimal.ZERO;
	private BigDecimal basicAmt = BigDecimal.ZERO;
	private int discountType;
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
	 private boolean check;
	
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;

}
