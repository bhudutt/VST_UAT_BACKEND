package com.hitech.dms.web.dao.common.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class HoModel {
	
	
	private BigInteger UserIdVsOrgHierId;
	private BigInteger hoUserId;
	private BigInteger orgHierarchyId;
	private char isActive;
	
	

}
