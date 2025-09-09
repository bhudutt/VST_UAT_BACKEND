/**
 * 
 */
package com.hitech.dms.web.model.grn.invoice.dtl.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoicesForGrnDtlForItemResponseModel {
	private BigInteger grnItemDtlId;
	private BigInteger machineItemId;
	private Integer invoiceQty;
	private String itemNo;
	private String itemDesc;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private BigDecimal unitPrice;
	private BigDecimal discountAmnt;
	private BigDecimal gstAmnt;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
	private Integer receiptQty;
	private String remarks;
	private String plantCode;
}
