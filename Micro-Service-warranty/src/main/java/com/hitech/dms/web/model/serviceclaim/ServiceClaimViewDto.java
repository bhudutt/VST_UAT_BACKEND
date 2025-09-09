package com.hitech.dms.web.model.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceClaimViewDto {
	
	private BigInteger id;
	
	private BigInteger invoiceId;
	
	private String invoiceNo;
	
	private Date invoiceDate;
	
	private String claimNo;
	
	private String claimType;
	
	private Date claimDate;
	
	private String status;
	
	private BigDecimal basePrice;
	
	private BigDecimal gstAmount;
	
	private BigDecimal invoiceAmount;
	
	private String remark;
	
	private String rejectedreason;
	
	private String customerInvoiceNo;
	private String customerInvoiceDate;
	private String finalSubmit;
	
	private List<ServiceClaimJobcardDetailDto> jobcardDetails;

}
