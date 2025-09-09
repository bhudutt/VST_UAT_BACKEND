/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.dtl.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchFormModel {
	private BigInteger empId;
	private List<AssignUserToBranchRequestModel> branchList;
}
