package com.hitech.dms.web.model.coupon.management;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class CouponSearchRequest {
	
	 private String fromDate;
	 private String toDate;
	 private String dealerCode;
	 private String docNo;
	 private Integer orgHierId;
	 private Integer hoId;
	 private Integer pcId;
	 private Integer zoneId;
	 private Integer territoryId;
	 private Integer dealerId;
	 private Integer branchId;
	 private Integer stateId;
	 private int size;
	 private int page;
	
	
	

}
