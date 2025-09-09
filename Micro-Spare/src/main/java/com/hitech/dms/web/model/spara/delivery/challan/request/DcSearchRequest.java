package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DcSearchRequest {
		
	
    private String profitCenter;
	
	private String dealership;
	
	private Integer dealerId; 
	
	private Integer branchId;
	
	private Integer stateId;
	
	private Integer pcId;
	
	private Integer zoneId;
	
	private Integer territoryId;
	
	private String branchName;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	private String dcNumber;
	
	private String dcStatus;
	
	private BigInteger partyTypeId;
	
	private BigInteger productCategoryId;
	
	private BigInteger productSubCategoryId;
	
	private BigInteger partyCodeId;
	
	private Integer page;
	
	private Integer size;
	
	
   
	
	
}
