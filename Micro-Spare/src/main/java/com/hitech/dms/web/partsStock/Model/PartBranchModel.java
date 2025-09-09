package com.hitech.dms.web.partsStock.Model;

import java.util.Date;

import lombok.Data;

@Data
public class PartBranchModel {
	
	private int partBranchId;
	private int partId;
	private int branchId;
	private int vendorId;
	private int brandId;
	private String  Abc;
	private String Fms;
	private double averageConsumption;
	private int minLevelQty;
	private int maxLevelQty;
	private int recordLevelQty;
	private Date lastSoqRunDate;
	private int movementClassId;
	private Date lastMvmtRunDate;
	private int safetyStockQty;
	private double stockAmount;
	private int inTransitQty;
	private int onHandQty;
	private int onOrderQty;
	private int customerOrderQty;
	private int pendingIndentRecvQty;
	private int pendingIndentlssQty;
	private char isSessional;
	private Date sessionFrom;
	private Date sessionTo;
	private double lastLandedCost;
	private double purchasePrice;
	private double sellingPrice;
	private double mrp;
	private char isAuctionable;
	private Date auctionDate;
	private String auctionRemarks;
	private int wipQty;
	private double warrantyRate;
	private String warrantyTaxCatgryCd;
	private Date lastTransactionDate;
	private Date lastUpdateDate;
	private String remarks;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private char lackOfTransaction;
	private double ndp;
	private String auctionablePart;
	private double auctionableQty;
	private int backOrder;
	private String ved;
	private int leadTime;
	private int accountCategoryId;
	private double auctionablePartRate;
	private double reOrderQty;
	private double vorQty;
	
	
	
	
	
	
	
	

}
