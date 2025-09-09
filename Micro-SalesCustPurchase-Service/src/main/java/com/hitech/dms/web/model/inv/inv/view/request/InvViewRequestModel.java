/**
 * 
 */
package com.hitech.dms.web.model.inv.inv.view.request;

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
public class InvViewRequestModel {
	private BigInteger salesInvoiceHdrId;
	private String invoiceNumber;
	private String isFor;
	private int flag;
}
