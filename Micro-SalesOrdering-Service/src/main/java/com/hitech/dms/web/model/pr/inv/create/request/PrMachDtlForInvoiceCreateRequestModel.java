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
public class PrMachDtlForInvoiceCreateRequestModel {
	private BigInteger purchaseReturnDtlId;
	private BigInteger grnDtlId;
	private BigInteger vinId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private Integer quantity;
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
//	private Integer receiptQty;
	private String remarks;
	private Boolean isDeleted;
}
