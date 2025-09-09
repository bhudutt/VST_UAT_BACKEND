package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CommonTerritoryModel {
	
	private BigInteger territoryId;
	private Integer StateId;
	private String territoryCode;
	private String territoryDesc;
	

}
