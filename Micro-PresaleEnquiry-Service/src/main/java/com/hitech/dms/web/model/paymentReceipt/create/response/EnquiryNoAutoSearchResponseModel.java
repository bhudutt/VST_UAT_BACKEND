package com.hitech.dms.web.model.paymentReceipt.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class EnquiryNoAutoSearchResponseModel {
	
	private BigInteger enquiryId;
	private String enquiryNo;
	private String branchCode;
//	private String customerName;
//	private String MobileNo;
	private BigInteger branchId;

}
