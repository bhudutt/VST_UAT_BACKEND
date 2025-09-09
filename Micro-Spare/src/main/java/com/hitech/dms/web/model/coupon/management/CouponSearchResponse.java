package com.hitech.dms.web.model.coupon.management;

import java.util.List;

import lombok.Data;

@Data
public class CouponSearchResponse {

	
	private List<SearchCouponDetailResponse> searchList;
	private CouponHeaderEntity headerDetails;
	private int StatusCode;
	private String statusMessage;
	private int totalRecordCount;
}
