package com.hitech.dms.web.model.digitalReport.request;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
@Data
public class DigitalEnquirySearchRequestModel {
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "fromDate", required = true)
	private Date fromDate;

	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "toDate", required = true)
	private Date toDate;
	
	
	private String fromDate1;
	private String toDate1;
	private Integer pcId;
	private Integer plateFormId;

	
	//@JsonFormat(pattern = "yyyy-MM-dd")
	
}
