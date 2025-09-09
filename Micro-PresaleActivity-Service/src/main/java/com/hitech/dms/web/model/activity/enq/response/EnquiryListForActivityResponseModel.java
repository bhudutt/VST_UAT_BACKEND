package com.hitech.dms.web.model.activity.enq.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryListForActivityResponseModel {
	private BigInteger enquiryId;
	private String enquiryNumber;
	private String enquiryDate;
	private String modelName;
	private String dspName;
	private String customerName;
	private String mobileNumber;
	private String tehsil;
	private String expectedPurchaseDate;
}
