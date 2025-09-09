/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchListResponseModel {
	private BigInteger branchEmpId;
	private BigInteger empId;
	private BigInteger branchId;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private Boolean isBranchPrimary;
	private Boolean isActive;
}
