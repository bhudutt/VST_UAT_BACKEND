package com.hitech.dms.web.model.partissue.search.request;

import lombok.Data;

@Data
public class PartIssueSearchRequestModel {

	private String fromDate;
	private String toDate;
	private String issueType;
	private String partIssueNo;
	private String jobCardNo;
	private String requisitionNo;
	private Integer page;
	private Integer size;
	
}
