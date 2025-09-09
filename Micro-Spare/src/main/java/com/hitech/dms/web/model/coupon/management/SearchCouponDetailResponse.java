package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"action","status","documentNo","documentDate","dealerCode","dealerName","branchName","branchLocation","totalCouponAmount","couponAmount",
	"creditNoteNo","creditNoteDate","creditNotAmount"})
public class SearchCouponDetailResponse {
	
	
	private String dealerCode;
	@JsonProperty("Approved Amount")
	private BigInteger couponAmount;
	private String status;
	private String documentNo;
	private String documentDate;
	//private BigInteger parentDealerId;
	private BigInteger totalCouponAmount;
	private String branchName;
	private String branchLocation;
	private String dealerName;
	private String action;
	private BigInteger creditNoteNo;
	private Date creditNoteDate;
	private BigInteger creditNotAmount;
	
	
	
	

}
