package com.hitech.dms.web.model.common.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OrgHierDealerList {
	
	
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
	private String displayName;
	

}
