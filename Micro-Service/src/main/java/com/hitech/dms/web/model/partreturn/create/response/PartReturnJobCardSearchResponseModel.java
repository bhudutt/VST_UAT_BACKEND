package com.hitech.dms.web.model.partreturn.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PartReturnJobCardSearchResponseModel {
	
	private Integer branchstoreId;
	private BigInteger issueId;
	private String JobCardNo;
	private String jobCardDate;
	private Integer branchId;
	private BigInteger roId;
	private String status;
	private String chassisNo;
	private String registrationNumber;
	private String modelVariant;
	private String firstname;
	
}
