package com.hitech.dms.web.model.enquiry.transfer.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class TransferENQResponse {
	private BigInteger enquiryId;
	private String enquiryNumber;
	private String enquiryDate;
	private String enquiryStatus;
	private String enquiryType;
	private String enquiryStage;
	private String salesman;
	private String modelPlusVariant;
	private String customerName;
	private String mobileNumber;
	private String village;
	private String tehsil;
	private String district;
	private String city;
	private String state;
	private String country;
}
