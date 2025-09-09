/**
 * 
 */
package com.hitech.dms.web.model.branchdtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class BranchDTLRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String isFor;
}
