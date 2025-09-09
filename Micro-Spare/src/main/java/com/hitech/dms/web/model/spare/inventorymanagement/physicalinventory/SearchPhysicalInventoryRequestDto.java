package com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SearchPhysicalInventoryRequestDto {
	
	private String phyInvNo;
	
	private String status;
	
	private BigInteger phyInvDoneBy;
	
	private BigInteger productCategoryId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
