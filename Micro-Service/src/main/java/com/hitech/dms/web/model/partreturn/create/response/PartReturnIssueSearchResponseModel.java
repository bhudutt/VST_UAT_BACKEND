package com.hitech.dms.web.model.partreturn.create.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PartReturnIssueSearchResponseModel {
    
	private Integer branchstoreId;
	private BigInteger roId;
	private BigInteger issueId;
	private String issueNo;
	private String issuedate;
	private String status;
	private String chassisNo;
	private String registrationNumber;
	private String modelVariant;
	private String firstname;
	
}
