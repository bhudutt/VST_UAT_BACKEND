/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.search.response;

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
public class ReceiptSearchResponseModel {
	private BigInteger id; // receiptId
	private String receiptNumber;
	private String receiptDate;
	private String receiptBy;
	private BigInteger id1; // dealerId
	private String dealerShip;
	private String profitCenter;
	private BigInteger id2; // branchId
	private BigInteger id3; // issueId
	private String issueNumber;
	private String issueDate;
	private String receiptFrom;
	private String receiptToBranch;
}
