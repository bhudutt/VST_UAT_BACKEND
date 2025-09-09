/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.view.response;

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
public class ReceiptViewResponseModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;
	private BigInteger receiptId;
	private BigInteger issueId;
	private String issueNumber;
	private String issueDate;
	private String receiptNumber;
	private String issueByName;
	private String issueRemarks;
	private String receiptDate;
	private BigInteger toBranchId;
	private String toBranch;
	private BigInteger receiptBy;
	private String receiptByName;
	private String remarks;
	private Integer totalIssuedQty;
	private List<ReceiptDtlViewResponseModel> receiptDtlList;
	private List<ReceiptItemViewResponseModel> receiptItemList;
}
