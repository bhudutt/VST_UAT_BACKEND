package com.hitech.dms.web.model.service.report.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RemiderSearchBean {
	
  private Integer page;
  
  private Integer size; 
  
  private String dealerCode;
  
  private Integer dealerId;
  
  private String customerCode;
  
  private Integer customerId;
  
  private String model;
  
  private String chassisNo;
  
  private String serviceName;
  
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date  fromDate;

  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date toDate;
  
  private Integer branchId; 
  
  private String zone;
  
  private Integer kpdId;
  
  private Integer modelId;
  
  private Integer stateId;
  
 

}
