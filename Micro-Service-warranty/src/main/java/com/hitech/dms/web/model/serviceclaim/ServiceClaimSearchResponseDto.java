package com.hitech.dms.web.model.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ServiceClaimSearchResponseDto {
	
	private BigInteger id;
	
	private String action;
	
	private String claimType;
	
	private String claimNo;
	
	private Date claimDate;
	
	private String status;
	
	private BigDecimal totalClaimValue;
	
	@JsonIgnore
    private Long totalCount;

    private String dealerCode;

    private String dealerName;

    private String dealerBranch;
    
    private String dealerLocation;

}
