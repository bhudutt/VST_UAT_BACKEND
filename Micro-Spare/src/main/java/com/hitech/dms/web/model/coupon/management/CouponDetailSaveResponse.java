package com.hitech.dms.web.model.coupon.management;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CouponDetailSaveResponse {

	
	private CouponHeaderEntity couponHeader;
	private List<CouponDetailsEntity> couponDetails;
	@JsonProperty(value="dealerCode")
	private String dealerCode;
	private int StatusCode;
	private String statusMessage;
	 
}
