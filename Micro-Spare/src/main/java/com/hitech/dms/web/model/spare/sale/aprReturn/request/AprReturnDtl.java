package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class AprReturnDtl {
	
	private BigInteger aprDetailId;
	
	private BigInteger aprReturedId;
	
	private BigInteger invoiceSaleDetailId;
	
	private BigInteger partBranchId;
	
	private BigInteger branchId;
	
	private BigInteger partId;
	
	private BigInteger stockBinId;
	
	private BigInteger stockStoreId;
	
	private BigDecimal mrp;
	
	private Integer aprReturnedQty;
	
	private Integer aprReturnQty;
	
	private String store;
	
	private Integer invoiceQty;
	
	private BigDecimal discountRate;
	
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
	
	
	
	
	

}
