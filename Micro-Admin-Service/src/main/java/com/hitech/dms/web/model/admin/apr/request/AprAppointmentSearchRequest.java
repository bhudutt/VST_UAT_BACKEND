package com.hitech.dms.web.model.admin.apr.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AprAppointmentSearchRequest {

	
	  private Integer page;
	  private Integer size;
	  @JsonFormat(pattern = "dd/MM/yyyy")
	  private Date fromDate;
	  @JsonFormat(pattern = "dd/MM/yyyy")
	  private Date toDate;
	  private Integer partyTypeId;
	  private Integer branchId;
	  private Integer partyCodeId;
	  private Integer invoiceId;
	  private String aprReturnNumber;
		      
}
