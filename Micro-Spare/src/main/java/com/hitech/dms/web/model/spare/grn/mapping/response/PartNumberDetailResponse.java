package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PartNumberDetailResponse {

	private BigInteger grnDtlId;
	private int partId;
	private int partBranchId;
	private BigInteger stockBinId;
	private Integer branchStoreId;
	private String storeDesc;
	private String partNumber;
	private String partDesc;
	private String partSubCategory;
	private String binLocation;
	private String hsnCode;
	private String serialNumber;
	private BigDecimal basicUnitPrice;
	private BigDecimal mrp;
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
	private Integer availableQty;
	private String poNumber;
	private String remarks;
	private BigDecimal restrictedQty;
	private BigDecimal unRestrictedQty;
	private BigDecimal claimedQty;
	private String claimStatus;
	private BigDecimal claimValue;
	private String isAgree;
	
}
