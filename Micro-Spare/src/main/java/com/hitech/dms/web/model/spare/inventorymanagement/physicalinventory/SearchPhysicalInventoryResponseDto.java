package com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SearchPhysicalInventoryResponseDto {
	
	private BigInteger id;
	
	private String phyInvNo;
	
	private String phyInvDate;
	
	private String productCategory;
	
	private String status;
	
	private String remarks;
	
	private String phyInvDoneBy;
	
	private String adjustmentNo;
	
	private String adjustmentStatus;
	
	
	@JsonIgnore
    private Long totalCount;

}
