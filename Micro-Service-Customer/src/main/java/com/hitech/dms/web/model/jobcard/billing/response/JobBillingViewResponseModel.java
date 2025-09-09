package com.hitech.dms.web.model.jobcard.billing.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class JobBillingViewResponseModel {
	private BigInteger id;
	private String jobCardBillNo;
	private String jobCardBillDate;
	private String billStatus;
	private String jobCardNumber;
	private String closeDate;
	private String saleDate;
	private String SaleType;
	private String paymentMode;
	private String chassisNo;
	private String engineNo;
	private String registrationNumber;
	private String modelVariant;
	private String vinNo;
	private String customerCode;
	private String customerType;
	private String customerName;
	private String mobileNo;
	private String companyName;
	private String pinCode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private BigDecimal basePrice;
	private BigDecimal discount;
	private BigDecimal taxableAmount;
	private BigDecimal invoiceAmount;
	private BigInteger gstAmount;

}
