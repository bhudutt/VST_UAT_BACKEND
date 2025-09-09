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
public class InvoicesForGrnDtlForMachineResponseModel {
	private BigInteger grnDtlId;
	private BigInteger machineItemId;
	private BigInteger vinId;
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
	private String plantCode;
	private String bToCFlag;
	
}
