package com.hitech.dms.web.model.coupon.management;

import java.util.List;

import lombok.Data;

@Data
public class CouponDetailResponse {

	
	List<CouponDetailsEntity> detailList;
	private CouponHeaderEntity headerDetail;
	private int statusCode;
	private String statusMessage;
}
