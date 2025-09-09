/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.view.request;

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
public class ReceiptViewRequestModel {
	private BigInteger receiptId;
	private int flag;
}
