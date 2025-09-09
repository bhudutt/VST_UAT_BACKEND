/**
 * 
 */
package com.hitech.dms.web.model.issue.indent.list.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IndentAutoListResponseModel {
	private BigInteger branchId;
	private String branchName;
	private BigInteger indentId;
	private String indentNumber;
	private String indentDate;
	private BigInteger indentBy;
	private String indentByName;
	private BigInteger indentToBranchId;
	private String indentToBranch;
	private String displayValue;
}
