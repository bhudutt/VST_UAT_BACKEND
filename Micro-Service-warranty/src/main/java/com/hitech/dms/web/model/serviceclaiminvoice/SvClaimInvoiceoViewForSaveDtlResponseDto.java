package com.hitech.dms.web.model.serviceclaiminvoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SvClaimInvoiceoViewForSaveDtlResponseDto {
	
	private BigInteger roId;
	
	private String jobcardNo;
	
	private Date jobcardDate;
	
	private String claimType;
	
	private BigInteger jobcardCatId;
	
	private String jobcardCategory;
	
	private BigInteger serviceTypeId;
	
	private String serviceType;
	
	private String model;
	 
	private String chassisNo;
	
	private String vinNo;
	
	private String engineNo;
	
	private BigInteger hours;
	
	private BigDecimal claimValue;
	
	private String plantCode;
	private BigInteger claimDtlId;
	
	private Integer cgstPercent;
	
	private BigDecimal cgstValue;
	
	private Integer sgstPercent;
	
	private BigDecimal sgstValue;
	
	private Integer igstPercent;
	
	private BigDecimal igstValue;
	
	private BigInteger pdiId;
	
	private BigInteger insId;

}
