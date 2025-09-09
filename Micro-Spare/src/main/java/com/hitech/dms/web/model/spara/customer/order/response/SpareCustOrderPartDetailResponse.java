package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareCustOrderPartDetailResponse {
	
	
	
		private Integer id;
		
		private Integer partId;
		
		private boolean select = false;
		
		private String partNo;
		
		private String partDesc;
		
		private String productSubCategory;
		
		private String HSNCode;
		
		private Integer currentStock;
		
		private Integer orderQty;
		
		private Integer invoicedQty;
		
		private BigDecimal basicUnitPrice;

		private BigDecimal mrp;

		private BigDecimal totalBasePrice;
		
		private BigDecimal cgst;
		
		private BigDecimal cgstAmount;
		
		private BigDecimal sgst;
		
		private BigDecimal sgstAmount;
		
		private BigDecimal igst;
		
		private BigDecimal igstAmount;
		
		private BigDecimal totalGst;
		
		private String store; 
		
		private String binlocation;
				
		private BigInteger balanceQty;
		
		private BigInteger issueQty;
		
		private Boolean productCtgFlag=false;
		
		private Integer partBranchId;
		
		private String msg;

}
