package com.hitech.dms.web.model.report.model;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InvoiceReportSearchRequest {

	private Integer branchId;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private String zone;
	
	private String custFlag;
	
	private String customerName;
	
	private BigInteger kpdId;
	
	private String kpd;
	
	private Integer stateId;
	
	private String partNumber;
	
	private Integer partCategoryId;
	
	private String dealerCode;
	
	private String HSNCode;
	
	private String bin;
	
	private String storeName;
	
	private Integer page;
	
	private Integer size;
}
