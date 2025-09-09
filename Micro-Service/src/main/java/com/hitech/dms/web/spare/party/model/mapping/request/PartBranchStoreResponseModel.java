package com.hitech.dms.web.spare.party.model.mapping.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartBranchStoreResponseModel {
	
	private String partNumber;
	private String partDesc;
	private int branchStoreId;
	private String storeCode;
	private String storeDesc;
	private String binName;
	private String isActive;
	private String isMainStore;
	private BigDecimal stockInHand;
	
	

	
	
	
	

}
