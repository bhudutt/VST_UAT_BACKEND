package com.hitech.dms.web.model.dealermaster.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DealerListResponseModel {
	
	private BigInteger dealerId;
	private String dealerName;
	private String dealerCode;
	
}
