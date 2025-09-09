/**
 * 
 */
package com.hitech.dms.web.model.grn.invoice.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoicesForGrnDtlRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private Integer grnTypeId;
	private BigInteger invoiceId;
	private String invoiceNo;
	private int flag;
}
