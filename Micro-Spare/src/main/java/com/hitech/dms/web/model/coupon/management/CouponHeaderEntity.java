package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;
@Data
public class CouponHeaderEntity {
	@JsonProperty(value="docNo")
	private String documentNo;
	@JsonProperty(value="docDate")
	private String documentDate;
	@JsonProperty(value="docStatus")
	private String documentStatus;
	@JsonProperty(value="approvedAmount")
	private BigInteger approvedTotalAmount;
	private String createdBy;
	private Date createdDate;
	

}
