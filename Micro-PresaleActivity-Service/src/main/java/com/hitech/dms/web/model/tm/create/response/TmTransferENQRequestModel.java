package com.hitech.dms.web.model.tm.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class TmTransferENQRequestModel {

	private Integer dealerId;
	private Integer branchID;
	private Integer tmPersonID;
	private String enquiryNo;
	private String enquiryFromDate;
	private String enquiryToDate;
	private String includeInactive;
}
