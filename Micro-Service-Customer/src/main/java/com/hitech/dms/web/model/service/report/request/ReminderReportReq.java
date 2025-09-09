package com.hitech.dms.web.model.service.report.request;

import java.math.BigInteger;

import lombok.Data;
@Data
public class ReminderReportReq {
	
	private String searchText;
	
	private BigInteger branchId;

}
