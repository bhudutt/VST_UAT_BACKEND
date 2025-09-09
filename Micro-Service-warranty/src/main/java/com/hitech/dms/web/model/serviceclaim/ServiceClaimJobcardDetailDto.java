package com.hitech.dms.web.model.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceClaimJobcardDetailDto {
	private BigInteger roId;
	
	private String jobcardNo;
	
	private Date jobcardDate;
	
	private BigInteger jobcardCatId;
	
	private BigInteger claimId;
	
	private String claimType;
	
	private String jobcardCategory;
	
	private String serviceType;
	
	private BigInteger serviceTypeId;
	
	private String model;
	
	private String chassisNo;
	
	private String vinNo;
	
	private String engineNo;
	
	private BigInteger hour;
	
	private BigDecimal claimValue;
	
	private Integer pdiId;
	private String pdiNo;
	private Date pdiDate;
	private Integer installationId;
	private String installationNo;
	private Date installationDate;
	private BigInteger dcId;
	private String dcNumber;
	private Date dcDate;
	
	private String installationStatus;

}
