package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ViewAprReturnDtlResponse {
	
private BigInteger aprDetailId;
	
	private String partNumber;
//	
	private String partDesc;
//	
	private String productCtg;
//	
	private Integer totalStock;
	
	private Integer orderQty;
//	
	private String binName;
	
	private BigDecimal mrp;
	
	private Integer aprReturnedQty;
	
	private Integer aprReturnQty;
	
	private String store;
	
	private Integer invoiceQty;
	
	private Integer discountRate;
	
	private BigDecimal discountValue;
	
	private BigDecimal cgstPer;
	
	private BigDecimal igstPer;
	
	private BigDecimal sgstPer;
	
	private BigDecimal cgstAmount;
	
	private BigDecimal igstAmount;
	
	private BigDecimal sgstAmount;
	
	private String additionalDiscountType;
	
	private BigDecimal additionalDiscountRate;
	
	private BigDecimal additionalDiscountAmount;
	
	private BigDecimal taxableAmount;
	
	private BigDecimal basicValue;
	
	private Integer partBranchId;
	
	private Integer partId;
	
	private Integer branchId;
	
	private Integer stockStoreId;	
	
	private BigInteger stockBinId;	
	
	private BigInteger invoiceDetailId;	
	

}
