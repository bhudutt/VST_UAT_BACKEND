/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.view.response;

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
public class AssignUserToBranchDTLResponseModel {
	private BigInteger dealerId;
	private String dealerName;
	private String dealerCode;
	private String dealerLocation;
	private BigInteger empId;
	private String empName;
	private String empCode;
	private List<AssignUserToBranchListResponseModel> branchList;
}
