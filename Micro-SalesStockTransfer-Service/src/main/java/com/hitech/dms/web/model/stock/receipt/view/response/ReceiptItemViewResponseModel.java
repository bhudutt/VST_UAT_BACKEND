/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.view.response;

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
public class ReceiptItemViewResponseModel {
	private BigInteger receiptItemId;
	private BigInteger issueItemId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private Integer receiptQty;
}
