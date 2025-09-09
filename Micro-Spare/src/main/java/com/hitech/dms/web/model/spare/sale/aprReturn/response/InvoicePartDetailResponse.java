package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class InvoicePartDetailResponse {
	
	
	  private BigInteger id;
	  private BigInteger partId;
	  private BigInteger binStoreId;
	  private Integer branchId;
	  private Integer stockStoreId;
	  private BigInteger partBranchId;
	  private String partNo;
	  private String partDescription;
	  private String productSubCat;
	  private Integer totalStock;
	  private BigDecimal orderQty;
	  private BigDecimal invoicedQty;
	  private Integer returnedQty;
	  private Integer returnQty;
	  private String store;
	  private String bin;
	  private BigDecimal mrp;
	  private BigDecimal discount;
	  private BigDecimal discountAmount;
	  private BigDecimal basicUnitPrice;
	  private BigDecimal additionalDiscountPer;
	  private BigDecimal additionalDiscountAmount;
	  private String AdditionalDiscountType;
	  private BigDecimal taxableAmount;
	  private Integer cgst;
	  private BigDecimal cgstAmount;
	  private Integer sgst;
	  private BigDecimal sgstAmount;
	  private Integer igst;
	  private BigDecimal igstAmount;
    


}
