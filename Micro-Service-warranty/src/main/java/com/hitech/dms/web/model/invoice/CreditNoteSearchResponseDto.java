package com.hitech.dms.web.model.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CreditNoteSearchResponseDto {

	private BigInteger id;
  	
    private String invoiceNo;

    private Date invoiceDate;
    
    private String warrantyType;
    
    private String wcrNo;
    
    private Date wcrDate;
    
    private String pcrNo;
    
    private Date pcrDate;

    private String chassisNo;
    
    private String vinNo;
    
    private String engineNo;
    
    private String registrationNo;
    
    private BigDecimal invoiceAmount;
    
    private String creditNoteNo;
    
    private Date creditNoteDate;
    
    private BigDecimal creditNoteAmount;

    @JsonIgnore
    private Long totalCount;
  	
  	private String action;
  	
  	private String dealerCode;

    private String dealerName;

    private String branch;
}
