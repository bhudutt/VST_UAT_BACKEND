package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class StateResponse {

	
	private BigInteger orgHierId;
	private String stateCode;
	private String stateDesc;
}
