package com.hitech.dms.web.model.report.model;

import java.util.Date;

import lombok.Data;

@Data
public class PartGrnSearchRequest {
//	private String zone;
//	
//	private String custFlag;
//	
	private String customerName;
	
	private String partNumber;
	
//	private BigInteger kpdId;
	
//	private Integer stateId;
	
	private Integer partCategoryId;
	
	private String dealerCode;
	
//	private String HSNCode;
//	
//	private String bin;
//	
//	private String storeName;
//	@JsonFormat(pattern = "dd/MM/yyyy")
	private String fromDate;
	
//	@JsonFormat(pattern = "dd/MM/yyyy")
	private String toDate;
	
	private Integer page;
	
	private Integer size;
	
	private Integer branchId;

}
