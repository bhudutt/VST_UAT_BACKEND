package com.hitech.dms.web.model.servicequotation.create.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceQuotationPartModel {

	private Integer quotationPartId;
	private Integer quotationId;
	private Integer partId;
	private String partNo;
	private String description;
	private Integer partBranchId;
	private BigDecimal qtyReq = BigDecimal.ZERO;
	private BigDecimal rate = BigDecimal.ZERO;
	private BigDecimal basicAmt = BigDecimal.ZERO;
	private Integer discountType;
	private BigDecimal discountRate = BigDecimal.ZERO;
	private BigDecimal discountAmt = BigDecimal.ZERO;
	private BigDecimal totalAmt = BigDecimal.ZERO;
	private BigDecimal taxAmt = BigDecimal.ZERO;
	private Integer serialNo;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private Boolean deletablePartId;
	 private boolean check;
	private ServiceQuotationModel serviceQuotationModel;
	private  BigDecimal netAmt=BigDecimal.ZERO;
}
