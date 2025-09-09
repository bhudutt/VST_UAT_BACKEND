package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class JobBillingSearchResponseModel {

	private BigInteger id;
//	private BigInteger ids;
	private String billingNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date billingDate;
//	private String action;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date saleDate;
	private String jobCardNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date jobCardDate;
	private String billingStatus;
	private String saleType;
	private String paymentMode;
	private String chassisNo;
	private String engineNo;
	private String registrationNumber;
	private String customerType;
	private String customerName;
	private String customerCode;
	private BigDecimal basePrice;
	private BigDecimal discount;
	private BigDecimal taxableAmount;
	private BigDecimal InvoiceAmount;
	
}
