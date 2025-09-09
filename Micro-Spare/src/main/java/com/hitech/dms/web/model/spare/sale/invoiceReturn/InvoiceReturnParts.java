package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class InvoiceReturnParts {
	
	private Integer partId;
	private String partNo;
	private String partDescription;
	private Integer invoiceQty;
	private Integer currentStock;
	private Integer claimQty;
	private BigDecimal unitPrice;
	private BigDecimal Discount;
	private BigDecimal BasicPrice;
	private BigDecimal GST_AMOUNT;
	private BigDecimal AccessableAmount;
	private BigDecimal totalValue;
	private String claimtype;
	private String claimDate ;
	private BigDecimal RecieptQuantity;
	private BigInteger partBranchId;
	private BigInteger stockBinId;
	private Integer branchStoreId;
	private String remarks;
	
	//private 
	
	
	//PartNumber	PartDesc
	//currentstock
	private String  partCategory;
	private Integer returnQty;
	private BigDecimal basicUnitPrice;
	private String  store;
	private String  binLocation;
	private BigDecimal price;
	private BigDecimal  gstPercent;
	private BigDecimal  gstTotal;
	private BigDecimal  basicAmount;
	private BigDecimal  totalGST;
	private BigDecimal  totalAmount;
	private String returnType;
	
	private BigDecimal vorCharge;
	
	
	
	
	
	

}
