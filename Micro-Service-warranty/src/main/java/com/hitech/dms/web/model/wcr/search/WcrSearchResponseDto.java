package com.hitech.dms.web.model.wcr.search;

import java.math.BigInteger;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class WcrSearchResponseDto {

	private BigInteger id;
	
	private String action;
	
	private String wcrNo;
	
	private Date wcrDate;
	
	private BigInteger pcrId;
  	
    private String pcrNo;

    private String status;

    private Date pcrCreatedDate;
    
    private BigInteger goodwillId;
    
//    private Date pcrSubmittedDate;

    private String jobCardNo;

    private String chassisNo;
    
    private String vinNo;
    
    private String engineNo;
    
    private BigInteger hour;

    private Date jobCardDate;
    
    private String typeOfClaim;

//    @JsonIgnore
//    private Long totalCount;
//
//    private String dealerCode;
//
//    private String dealerName;
//
//    private String branch;
}
