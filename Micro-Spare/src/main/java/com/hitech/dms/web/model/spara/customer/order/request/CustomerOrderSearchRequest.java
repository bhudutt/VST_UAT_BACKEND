package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CustomerOrderSearchRequest {
	
	private String profitCenter;
	
	private String dealership;
	
	private String branchName;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	
	private String customerOrderStatus;
	
	private String customerOrderNumber;
	
	private BigInteger partyTypeId;
	
	private BigInteger productCategoryId;
	
	private BigInteger productSubCategoryId;
	
	private BigInteger partyCodeId;
	
	private BigInteger pcId;
	
	private BigInteger zoneId;
	
	private BigInteger dealerId;
	
	private BigInteger territoryId;
	
	private BigInteger stateId;
	
	private BigInteger branchId;
	
	
	private Integer page;
	
	private Integer size;

}
