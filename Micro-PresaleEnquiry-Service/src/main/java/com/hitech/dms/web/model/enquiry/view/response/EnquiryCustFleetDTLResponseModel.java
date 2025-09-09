package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class EnquiryCustFleetDTLResponseModel {
	private BigInteger enq_machinery_id;
	private String brandName;
	private String model;
	private int yearOfPurchase;
	private BigInteger brandId;
}
