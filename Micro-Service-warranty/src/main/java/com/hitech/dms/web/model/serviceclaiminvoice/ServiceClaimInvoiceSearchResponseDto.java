package com.hitech.dms.web.model.serviceclaiminvoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ServiceClaimInvoiceSearchResponseDto {
	
	private BigInteger id;
	
	private String invoiceNo;
	
	private Date invoiceDate;
	private String finalSubmit;
	
	private String claimType;
	
	private String claimNo;
	
	private Date claimDate;
	
	private BigDecimal claimAmount;
	
	private String creditNoteNo;
	
	private Date creditNoteDate;
	
	private BigDecimal creditNoteAmount;
	
//	@JsonIgnore
//    private Integer totalCount;

    private String dealerCode;

    private String dealerName;

    private String dealerBranch;
    
    private String dealerLocation;
    
    private String action;
    
    private String customerInvoiceNo;
    private String customerInvoiceDate;
    

}
