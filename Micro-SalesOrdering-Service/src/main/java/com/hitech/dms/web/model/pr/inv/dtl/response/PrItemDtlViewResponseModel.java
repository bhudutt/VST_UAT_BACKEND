/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.dtl.response;

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
public class PrItemDtlViewResponseModel {
	private BigInteger purchaseReturnItemId;
	private BigInteger grnItemDtlId;
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
	
	private BigDecimal cgstPer;
	private BigDecimal sgstPer;
	private BigDecimal igstPer;
	private BigDecimal cgstAmnt;
	private BigDecimal sgstAmnt;
	private BigDecimal IgstAmnt;
}
