package com.hitech.dms.web.model.enquiry.export;

import java.util.List;

import lombok.Data;

@Data
public class EnquiryReport2Response {

	
	private List<EnquiryResponse>  enquiryList;
	private String  statusCode;
	private String message;
	private Integer totalRecords;
}
