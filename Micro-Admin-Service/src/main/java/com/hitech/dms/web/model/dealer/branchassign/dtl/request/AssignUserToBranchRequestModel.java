/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchRequestModel {
	private BigInteger branchEmpId;
	private BigInteger empId;
	private BigInteger branchId;
	private Boolean isBranchPrimary;
	private Boolean isActive;
}
