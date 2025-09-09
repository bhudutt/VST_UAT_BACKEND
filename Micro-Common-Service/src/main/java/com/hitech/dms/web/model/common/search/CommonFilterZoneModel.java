package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CommonFilterZoneModel {
	
	private BigInteger orgZoneHierarchyId;
	private Integer levelId;
	private BigInteger orgHierarchyId;
	private String zoneCode;
	private String zoneDesc;
	

}
