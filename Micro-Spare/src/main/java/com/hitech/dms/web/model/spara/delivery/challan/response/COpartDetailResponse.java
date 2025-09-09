package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class COpartDetailResponse {
	
	private Integer customerDtlId;
	
	private Integer partId;
	
	private String partNumber;
	
	private String partDesc;
	
	private String productSubCategory;
	
	private String customerOrderNumber;
	
	private Date customerOrderDate;
	
	private Integer totalStock;
	
	private Integer customerOrderQty;
	
	private BigDecimal basicUnitPrice;
	
	private String hsnCode;
	
	private BigDecimal sgstPer;
	
	private BigDecimal igstPer;
	
	private BigDecimal cgstPer;
	
	private BigDecimal partStoreCount;
	
	private Integer partBranchId;
	
	private String storeNames;
	
	private String binLocations;
	
	private BigInteger coBalanceQty;
	
	//added for inventory
	
	private BigInteger stockBinid;
	
	private Integer storeId;
	
	private Integer stockStoreId;
	
	private Integer branchId;
	
	private BigInteger binId;
	
	private BigDecimal dealerMRP;
	
	private Integer totalSumOfIssue;
	
	private BigDecimal cgstAmount;
	private BigDecimal  sgstAmount;
	private BigDecimal  igstAmount;
	private BigDecimal totalGst;
	private BigDecimal  mrp;
	private BigDecimal totalBasePrice;
	
	private String picklistnumber;
	
	private Integer pickListDtlId;
	

	
	
}
