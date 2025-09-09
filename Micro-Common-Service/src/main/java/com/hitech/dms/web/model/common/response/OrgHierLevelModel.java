package com.hitech.dms.web.model.common.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OrgHierLevelModel {

	
	private BigInteger orgHierId;
	private Integer levelId;
	private String hierarchyCode;
	private String hierarchyDesc;
	private BigInteger parentorgHierarchyId;
	
	
}
