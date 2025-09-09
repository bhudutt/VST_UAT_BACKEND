package com.hitech.dms.web.model.digitalUpload.create.request;

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

	
	//@JsonFormat(pattern = "yyyy-MM-dd")
	
}
