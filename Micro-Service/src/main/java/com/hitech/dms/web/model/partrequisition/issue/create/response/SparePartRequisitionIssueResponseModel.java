package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class SparePartRequisitionIssueResponseModel {

	private String msg;
	private Integer statusCode;
	private BigInteger issueId;
	private String issueNumber;
	
}
