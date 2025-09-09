/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.create.request;

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
public class PrItemDtlForInvoiceCreateRequestModel {
	private BigInteger purchaseReturnItemId;
//	private BigInteger grnItemDtlId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private Integer invoiceQty;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private BigDecimal unitPrice;
	private BigDecimal discountAmnt;
	private BigDecimal igstPer;
	private BigDecimal igstAmount;
	private BigDecimal cgstPer;
	private BigDecimal cgstAmount;
	private BigDecimal sgstPer;
	private BigDecimal sgstAmount;
	private BigDecimal totalGstAmount;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
	private Integer returnQuantity;
	private String remarks;
	private transient boolean isDeleted;
}
