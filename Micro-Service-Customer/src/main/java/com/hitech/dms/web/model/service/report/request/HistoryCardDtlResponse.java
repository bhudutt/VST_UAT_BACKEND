package com.hitech.dms.web.model.service.report.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class HistoryCardDtlResponse {

	private String Date;
	private BigInteger roId;
	private String RONumber;
	private BigInteger Total_Hour;
	private String jobCardcategory;
	private String serviceType;
	private String SrvTypeDesc;
	private String observation;
	private String activityDone;
	private String actiontaken;
	private String partreplaced;
	private String statusofjob;
}
