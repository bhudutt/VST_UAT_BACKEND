package com.hitech.dms.web.model.serviceclaiminvoice;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class ServiceClaimInvoiceSearchRequestDto {
	
	private String invoiceNo;
	
	private String claimNo;
	
	private BigInteger claimTypeId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
