/**
 * 
 */
package com.hitech.dms.web.model.issue.indent.dtl.response;

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
public class IndentDtlResponseModel {
	private BigInteger branchId;
	private String branchName;
	private BigInteger indentId;
	private String indentNumber;
	private String indentDate;
	private BigInteger indentBy;
	private String indentByName;
	private BigInteger indentToBranchId;
	private String indentToBranch;
	private String indentRemarks;
	private List<IndentMachDtlResponseModel> indentDtlList;
	private List<IndentItemDtlResponseModel> indentItemList;
}
