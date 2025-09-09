/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ReceiptItemCreateRequestModel {
	private BigInteger issueItemId;
	private Integer receiptQty;
}
