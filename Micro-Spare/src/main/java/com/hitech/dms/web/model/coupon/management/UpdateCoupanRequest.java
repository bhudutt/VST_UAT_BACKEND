package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;

import lombok.Data;

@Data
public class UpdateCoupanRequest {
	
	
	private BigInteger detailId;
	private String punchingDate;
	private Integer couponNo;
	private Integer couponValue;
	private String approvalStatus;
	private String remarks;

}


//[
// {
//	 "detailId":2,
//	 "punchingDate":"12/3/20",
//	 "couponNo":212,
//	 "approvalStatus":"approved"
// } 
// {
//	 "detailId":3,
//	 "punchingDate":"12/3/20",
//	 "couponNo":213,
//	 "approvalStatus":"approved"
// } 
// {
//	 "detailId":4,
//	 "punchingDate":"12/3/20",
//	 "couponNo":216,
//	 "approvalStatus":"Reject"
// } 
// {
//	 "detailId":5,
//	 "punchingDate":"12/3/20",
//	 "couponNo":217,
//	 "approvalStatus":null
// } 
//]
		