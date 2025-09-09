package com.hitech.dms.web.spare.party.model.mapping.request;

import lombok.Data;

@Data
public class PartBranchRequestModel {
	
	private int partBranchId;
	private  int partId;
	private int accountCategoryId;
	private int branchId;
	private float mrp;
	private float ndp;
	private float sellingPrice;
	private int minLevelQty;
	private int maxLevelQty;
	private int recordLevelQty;
	private float reserveLevelQty;
	private float reOrderQty;
	private int movementClassId;
	private String abc;
	private String fms;
	private String ved;
	private String isSeasonal;
	private float vorQty;
	private String isAuctionable;
	private String auctionablePart;
	private float auctionableQty;
	private int onHandQty;
	private  float stockAmount;
	private int backOrderQty;
	private int wipQty;
	private int inTransitQty;
	
}
