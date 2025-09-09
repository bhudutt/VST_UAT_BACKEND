package com.hitech.dms.web.model.serviceclaim;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceClaimSearchRequestDto {
	
	private String claimNo;
	
	private String userCode;
	
	private BigInteger claimTypeId;
	
	private String status;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
