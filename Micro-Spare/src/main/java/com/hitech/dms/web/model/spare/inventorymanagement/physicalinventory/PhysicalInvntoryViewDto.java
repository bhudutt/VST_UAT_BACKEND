package com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PhysicalInvntoryViewDto {
	
	private BigInteger id;
	
	private BigInteger branchId;
	
	private String branchName;
	
	private String phyInvNo;
	
	private Date phyInvDate;
	
	private BigInteger productCatId;
	
	private String productCat;
	
	private String status;
	
	private String remarks;
	
	private BigInteger phyInvDoneById;
	
	private String phyInvDoneBy;
	
	private BigDecimal isZeroQty;
	
	private BigInteger adjustmentId;
	
	private String adjustmentNo;
	
	private String adjStatus;
	
	private BigDecimal totatIncreseQty;
	
	private BigDecimal totatIncreseValue;
	
	private BigDecimal totatDecreaseQty;
	
	private BigDecimal totatDecreaseValue;
	
	private List<PhysicalInventoryDetailDto> partDetails;

}
