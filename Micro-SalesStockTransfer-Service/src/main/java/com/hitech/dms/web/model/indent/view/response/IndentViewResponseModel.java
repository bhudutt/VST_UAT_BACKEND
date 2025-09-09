/**
 * 
 */
package com.hitech.dms.web.model.indent.view.response;

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
public class IndentViewResponseModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;
	private BigInteger indentId;
	private String indentNumber;
	private String indentDate;
	private BigInteger indentBy;
	private String indentByName;
	private BigInteger indentToBranchId;
	private String indentToBranch;
	private String remarks;
	private List<IndentDtlViewResponseModel> indentDtlList;
	private List<IndentItemViewResponseModel> indentItemList;
}
