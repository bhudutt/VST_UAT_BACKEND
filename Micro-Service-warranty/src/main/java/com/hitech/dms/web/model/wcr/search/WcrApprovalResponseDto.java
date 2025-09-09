package com.hitech.dms.web.model.wcr.search;

import lombok.Data;

@Data
public class WcrApprovalResponseDto {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;

}
