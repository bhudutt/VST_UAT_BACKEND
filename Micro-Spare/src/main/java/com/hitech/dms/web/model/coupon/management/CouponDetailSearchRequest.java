package com.hitech.dms.web.model.coupon.management;

import lombok.Data;

@Data
public class CouponDetailSearchRequest {
	
	
	private String dealerCode;
	private String documentNo;
	private String searchType;
	private String userCode;

}
