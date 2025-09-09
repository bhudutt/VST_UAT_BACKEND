package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CommonFilterStateModel {

	private BigInteger stateId;
	private Integer orgStateId;
	private String stateCode;
	private String stateDesc;
	
	
	
	
}
