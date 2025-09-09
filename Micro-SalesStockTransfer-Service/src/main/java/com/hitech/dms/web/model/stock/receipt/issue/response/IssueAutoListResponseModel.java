/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.issue.response;

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
public class IssueAutoListResponseModel {
	private BigInteger branchId;
	private String branchName;
	private BigInteger issueId;
	private String issueNumber;
	private String issueDate;
	private BigInteger issueBy;
	private String issueByName;
	private BigInteger issueToBranchId;
	private String issueToBranch;
	private String displayValue;
}
