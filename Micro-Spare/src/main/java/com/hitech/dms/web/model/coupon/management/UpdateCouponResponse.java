package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class UpdateCouponResponse {

	
	private List<UpdateCoupanRequest> updateRequest;
	private String documentNo;
	private BigInteger approvedAmount;
	private int statusCode;
	private String statusMessage;
}
