package com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class DeadStockUploadResponseDto {
	private String dealerCode;
	
	private String dealerName;
	
	private String dealerLocation;
	
	private Integer partId;

	private String partNo;
	
	private String partDesc;
	
	private BigInteger prodCategoryId;
	
	private String productCategory;
	
	private BigInteger prodSubCategoryId;
	
	private String productSubCategory;
	
	private BigDecimal mrp;
	
	private Date dateOfPacking;
	
	private BigDecimal currentStock;
	
	private BigDecimal deadStockQty;
	
	private String fileName;
	
	private String fileType;
	
	@JsonIgnore
    private Long recordCount;

}
