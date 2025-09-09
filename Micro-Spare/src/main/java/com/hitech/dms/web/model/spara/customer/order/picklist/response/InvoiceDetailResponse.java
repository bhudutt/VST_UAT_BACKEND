package com.hitech.dms.web.model.spara.customer.order.picklist.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class InvoiceDetailResponse {

	
	private BigInteger invoiceSaleDtlId;
	private String PartNumber; 
	private String PartDesc;
	private BigDecimal totalStock;
	private BigDecimal MRP; 
	private BigDecimal BasicValue;
	private BigDecimal Qty; 
	private BigDecimal DiscountRate; 
	private BigDecimal DiscountValue; 
	private BigDecimal Add_Discount_Amount; 
	private BigDecimal Add_Discount_Rate; 
	private BigDecimal TaxValue; 
	private BigDecimal BillValue;
	private String StoreBinLocation;
	private String FromStore;
}
