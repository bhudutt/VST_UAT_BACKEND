package com.hitech.dms.web.model.goodwill;

import java.math.BigInteger;

import lombok.Data;

@Data
public class GoodwillApprovalRequestDto {

	private BigInteger goodwillId;
	private String goodwillNo;
	private String approvalStatus;
	private String remarks;
	private String rejectReason;
}
