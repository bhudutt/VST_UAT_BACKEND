package com.hitech.dms.web.model.invoice;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class InvoiceSearchResponseDto {

	private BigInteger id;
  	
  	private String action;
  	
    private String invoiceNo;
    private String finalSubmit;

    private String status = "Open";

    private Date invoiceDate;
    
    
    private String wcrNo;
    
    private Date wcrDate;
    
    private String pcrNo;
    
    private Date pcrDate;

    private String jobCardNo;
    
    private Date jobCardDate;

    private String chassisNo;
    
    private String vinNo;
    
    private String engineNo;

    @JsonIgnore
    private Long totalCount;

    private String dealerCode;

    private String dealerName;

    private String branch;
    
    
}
