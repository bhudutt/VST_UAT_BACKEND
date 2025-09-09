package com.hitech.dms.web.model.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceClaimJCListResponse {
	
	private BigInteger jobcardId;
	private String jobcardNo;
	private Date jobcardDate;
	private Integer claimId;
	private String claimType;
	private Integer jobcardCategoryId;
	private String jobcardCategory;
	private Integer serviceTypeId;
	private String serviceType;
	private String model;
	private String chassisNo;
	private String vinNo;
	private String engineNo;
	private BigInteger hours;
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
	private String plantCode;//added by mahesh.kumar on 20-03-2025
	
}
