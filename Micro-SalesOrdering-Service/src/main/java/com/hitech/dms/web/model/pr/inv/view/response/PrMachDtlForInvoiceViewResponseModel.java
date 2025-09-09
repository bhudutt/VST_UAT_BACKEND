/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.view.response;

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
public class PrMachDtlForInvoiceViewResponseModel {
	private BigInteger purchaseReturnInvDtlId;
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
	private BigDecimal igst_per;
	private BigDecimal igst_amount;
	private BigDecimal cgst_per;
	private BigDecimal cgst_amount;
	private BigDecimal sgst_per;
	private BigDecimal sgst_amount;
	private BigDecimal total_gst_amount;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
	private Integer invoiceQty;
	private Integer receiptQty;
	private String remarks;
}
