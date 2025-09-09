package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class InvoicePartDetailResponse {
	
	
	  private BigInteger id;
	  
	  private BigInteger partId;
   
	  private String partNo;
	  private String partDescription;
	  private String productSubCat;
	  private String totalStock;
	  private Integer orderQty;
	  private Integer invoicedQty;
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
	  private BigDecimal taxableAmount;
	  private BigDecimal cgst;
	  private BigDecimal cgstAmount;
	  private BigDecimal sgst;
	  private BigDecimal sgstAmount;
	  private BigDecimal igst;
	  private BigDecimal igstAmount;
    


}
