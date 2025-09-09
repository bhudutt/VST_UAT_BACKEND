/**
 * 
 */
package com.hitech.dms.web.model.inv.calculatAmnt.request;

import java.math.BigDecimal;
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
public class CaltemAmntForInvoiceRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger toDealerId;
	private BigInteger poHdrId;
	private BigInteger poDtlId;
	private BigInteger dcId;
	private BigInteger machineDcDtlId;
	private BigInteger dcItemId;
	private int qty;
	private BigDecimal rate;
	private BigDecimal discount;
	private Integer invoiceTypeId;
}
