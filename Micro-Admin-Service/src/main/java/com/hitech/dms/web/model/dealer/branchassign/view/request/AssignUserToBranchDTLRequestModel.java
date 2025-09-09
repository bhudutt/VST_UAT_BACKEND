/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchDTLRequestModel {
	private BigInteger dealerId;
	private BigInteger empId;
}
