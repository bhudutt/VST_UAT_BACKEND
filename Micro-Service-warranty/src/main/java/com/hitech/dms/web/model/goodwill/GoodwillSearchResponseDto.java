package com.hitech.dms.web.model.goodwill;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GoodwillSearchResponseDto {

	private BigInteger id;

    private String action;
	
	private String goodwillNo;
	
	private Date goodwillDate;
	
//	private Date dateOfInstallation;
//	
//	private BigInteger totalHour;
	
	private BigInteger pcrId;
  	
    private String pcrNo;

    private String status;

    private Date pcrCreatedDate;
    
    private Date pcrSubmittedDate;

    private String jobCardNo;

    private String chassisNo;

    private Date jobCardDate;

    @JsonIgnore
    private Long totalCount;

    private String dealerCode;

    private String dealerName;

    private String branch;
}
