package com.hitech.dms.web.model.baymaster.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BayMasterResponse {
	
	private BigInteger id;
	private String bayCode;
	private String msg;
	private int statusCode;

}
