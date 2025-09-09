/**
 * 
 */
package com.hitech.dms.web.model.branchdtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class BranchDTLResponseModel {
	private BigInteger branchId;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private String displayValue;
}
