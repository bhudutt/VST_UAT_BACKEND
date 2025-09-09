package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CouponDetailsEntity {
	
	private BigInteger couponHdrDtlId;
	private  String documentNo;
	private String punchingDate;
	@JsonProperty(value="couponNo")
	private Integer couponNumber;
	private BigInteger couponValue;
	private String approvedStatus;
	private BigInteger approvedTotalAmount;
	private String remarks;
	private String createdBy;
	private Date createdDate;
	

}
