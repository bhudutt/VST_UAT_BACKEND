/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchSearchResponseModel {
	private BigInteger branchUserId;
	private BigInteger dealerId;
	private BigInteger empId;
	private String empName;
	private String branchName;
	private String isActive;
	
}
