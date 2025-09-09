package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AprReturnSearchRequest {

	
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
	  private Integer stateId;
	  private String aprReturnNumber;
	  private String status;
		      
}
