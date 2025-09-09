package com.hitech.dms.web.model.wcr.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class WCRApprovalRequestDto {
	private BigInteger wcrId;
	private String wcrNo;
	private String approvalStatus;
	private String remarks;
	private String rejectReason;

}
