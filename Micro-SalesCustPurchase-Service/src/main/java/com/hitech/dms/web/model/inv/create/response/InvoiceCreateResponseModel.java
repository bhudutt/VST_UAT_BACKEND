/**
 * 
 */
package com.hitech.dms.web.model.inv.create.response;

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
public class InvoiceCreateResponseModel {
	private BigInteger salesInvoiceHdrId;
	private String invoiceNumber;
	private String msg;
	private Integer statusCode;
}
