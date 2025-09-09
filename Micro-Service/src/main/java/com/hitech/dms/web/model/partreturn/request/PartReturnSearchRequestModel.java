package com.hitech.dms.web.model.partreturn.request;

import lombok.Data;

@Data
public class PartReturnSearchRequestModel {
	
 private String fromDate;
 private String todate;
 private String returnType;
 private String partReturnNo;
 private Integer page;
 private Integer size;
 
}
