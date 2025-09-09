/**
 * 
 */
package com.hitech.dms.web.model.branch;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class UserBranchListModel {
	private BigInteger branchID;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private String isDefaultBranch;
	private BigInteger dealerID;
}
