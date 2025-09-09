/**
 * 
 */
package com.hitech.dms.web.model.inv.cancel.response;

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
public class InvCancelResponseModel {
	private BigInteger salesInvoiceHdrId;
	private String invoiceNumber;
	private String msg;
	private Integer statusCode;
}
