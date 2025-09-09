package com.hitech.dms.web.model.org.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OrgLeveDealerResponseModel {
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
	private String displayName;
}
