package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigDecimal;
import java.math.BigInteger;


import lombok.Data;

@Data
public class PartNumberDetailResponse {

	private int partId;
	private BigInteger stockBinId;
	private String partNumber;
	private String partDesc;
	private String partSubCategory;
	private String binLocation;
	private String hsnCode;
	private String serialNumber;
	private BigDecimal basicUnitPrice;
	private BigDecimal invoiceQty;
	private BigDecimal accessibleValue;
	private BigDecimal discountPercentage;
	private BigDecimal discountAmount;
	private BigDecimal cgstPercentage;
	private BigDecimal cgstAmount;
	private BigDecimal sgstPercentage;
	private BigDecimal sgstAmount;
	private BigDecimal igstPercentage;
	private BigDecimal igstAmount;
	private BigDecimal receiptQty;
	private BigDecimal receiptValue;
	
	private String storeDesc;
	private Integer BranchStoreId;
	
	
}
