/**
 * 
 */
package com.hitech.dms.web.model.grn.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class SalesGrnItemDtlCreateRequestModel {
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private Integer invoiceQty;
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
