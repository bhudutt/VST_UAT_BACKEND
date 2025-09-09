/**
 * 
 */
package com.hitech.dms.web.model.grn.invoice.autosearch.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoicesForGrnResponseModel {
	private BigInteger invoiceId;
	private String invoiceNo;
	private String invoiceType;
	private String displayValue;
}
