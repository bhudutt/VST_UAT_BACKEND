package com.hitech.dms.web.model.partissue.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PartIssueSearchResponseModel {
	
	private BigInteger 	id;
	private String 	issueNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String  issueDate;
	private String IssueType;
	private String issueBY;
	//private String requestedBy;
	private String requisitionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String requisitionDate;
	private String jobCardNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String jobCardDate;
	
	

}
