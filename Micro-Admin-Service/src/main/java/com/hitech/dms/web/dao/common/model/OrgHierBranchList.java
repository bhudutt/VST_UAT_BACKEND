package com.hitech.dms.web.dao.common.model;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OrgHierBranchList {

	private BigInteger branchId;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private String displayName;
	
	
}
