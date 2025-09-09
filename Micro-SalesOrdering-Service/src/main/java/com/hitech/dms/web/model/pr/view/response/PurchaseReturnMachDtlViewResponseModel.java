/**
 * 
 */
package com.hitech.dms.web.model.pr.view.response;

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
public class PurchaseReturnMachDtlViewResponseModel {
	private BigInteger purchaseReturnDtlId;
	private BigInteger grnDtlId;
	private BigInteger vinId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private Integer invoiceQty;
	private BigDecimal unitPrice;
	private BigDecimal discountAmnt;
	private BigDecimal gstAmnt;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
	private Integer receiptQty;
	private String remarks;
}
