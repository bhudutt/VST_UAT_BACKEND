/**
 * 
 */
package com.hitech.dms.web.model.pr.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class PurchaseReturnMachDtlCreateRequestModel {
	private BigInteger purchaseReturnDtlId;
	@JsonProperty(value = "grnDtlId", required = true)
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
	@JsonProperty(value = "receiptQty", required = true)
	private Integer receiptQty;
	@JsonProperty(value = "remarks", required = true)
	private String remarks;
	@JsonProperty(value = "isDeleted", required = true)
	private boolean isDeleted;
}
