/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.response;

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
public class ReceiptCreateResponseModel {
	private BigInteger receiptId;
	private String receiptNumber;
	private String msg;
	private Integer statusCode;
}
