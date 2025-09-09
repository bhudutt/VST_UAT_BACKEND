package com.hitech.dms.web.model.pcr;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class PcrSearchResponseDto {
	  	private BigInteger id;
	  	
	  	private String action;
	  	
	    private String pcrNo;

	    private String status;

	    private Date pcrCreatedDate;
	    
	    private Date pcrSubmittedDate;

	    private String jobCardNo;

	    private String chassisNo;

	    private Date jobCardDate;

//	    private Integer totalCount;
   
	    private String dealerCode;
    
	    private String dealerName;
    
	    private String branch;

}
