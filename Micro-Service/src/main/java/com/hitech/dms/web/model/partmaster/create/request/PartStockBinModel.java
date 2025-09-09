package com.hitech.dms.web.model.partmaster.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PartStockBinModel {

	private BigInteger stockBinId;
	private Integer branchId;
	private Integer partBranchId;
	private BigInteger stockStoreId;
	private String binName;
	private String binType;
	private BigDecimal currentStock;
	private BigDecimal currentStockAmount;
	private Boolean deafultStatus;
	private Boolean binBlocked;
	private Date lastUpdatedDate;
	private Boolean activeStatus;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	
	private Boolean partDetailGridSelect;
	private Integer branchStoreId;
	private String branchStoreName;
 
	private Boolean checkStockBinExistsInDBFlag;
}
