/**
 * 
 */
package com.hitech.dms.web.model.grn.view.response;

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
public class SalesGrnViewMachineDtlResponseModel {
	private BigInteger grnDtlId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private String chassisNo;
	private BigInteger vinId;
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
