package com.hitech.dms.web.model.common.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class HoModel {
	
	
	private BigInteger UserIdVsOrgHierId;
	private BigInteger hoUserId;
	private BigInteger orgHierarchyId;
	private String hoName;
	private char isActive;
	
	

}
