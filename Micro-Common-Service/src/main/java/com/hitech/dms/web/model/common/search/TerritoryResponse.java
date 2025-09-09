package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class TerritoryResponse {
	
	private BigInteger orgHierId;
	private String territoryCode;
	private String territoryDesc;

}
