package com.hitech.dms.web.model.org.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class OrgLevelDealerBranchResponseModel {
	private BigInteger branchId;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private String displayName;
}
