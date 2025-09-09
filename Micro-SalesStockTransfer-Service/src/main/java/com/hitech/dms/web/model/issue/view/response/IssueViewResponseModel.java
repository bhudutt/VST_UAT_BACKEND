/**
 * 
 */
package com.hitech.dms.web.model.issue.view.response;

import java.math.BigInteger;
import java.util.List;

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
public class IssueViewResponseModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;
	private BigInteger issueId;
	private String issueNumber;
	private String issueDate;
	private BigInteger indentId;
	private String indentNumber;
	private String indentDate;
	private BigInteger toBranchId;
	private String toBranch;
	private BigInteger issueBy;
	private String issueByName;
	private String indentByName;
	private String remarks;
	private Integer totalIssuedQty;
	private List<IssueDtlViewResponseModel> issueDtlList;
	private List<IssueItemViewResponseModel> issueItemList;
}
