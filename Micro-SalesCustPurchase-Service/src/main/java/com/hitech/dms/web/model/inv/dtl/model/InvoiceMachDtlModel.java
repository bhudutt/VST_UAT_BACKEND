/**
 * 
 */
package com.hitech.dms.web.model.inv.dtl.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class InvoiceMachDtlModel {
	private BigInteger machineItemId;
	private BigInteger machineInventoryId;
	private String itemNo;
	private String itemDesc;
	private String model;
	private String chassisNo;
	private BigInteger vinId;
	private String vinNo;
	private String engineNo;
	private String hsnCode;
	private Integer qty;
	private BigDecimal unitPrice;
	private BigDecimal discountAmnt;
	private BigDecimal igst_per;
	private BigDecimal igst_amount;
	private BigDecimal cgst_per;
	private BigDecimal cgst_amount;
	private BigDecimal sgst_per;
	private BigDecimal sgst_amount;
	private BigDecimal total_gst_per;
	private BigDecimal total_gst_amount;
	private BigDecimal assessableAmnt;
	private BigDecimal totalAmnt;
	private String remarks;
}
