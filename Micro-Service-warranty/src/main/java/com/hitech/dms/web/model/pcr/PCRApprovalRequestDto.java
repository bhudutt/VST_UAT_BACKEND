package com.hitech.dms.web.model.pcr;

import java.math.BigInteger;

import lombok.Data;

@Data
public class PCRApprovalRequestDto {
	private BigInteger pcrHdrId;
	private String pcrNo;
	private String approvalStatus;
	private String remarks;
	private String rejectReason;

}
