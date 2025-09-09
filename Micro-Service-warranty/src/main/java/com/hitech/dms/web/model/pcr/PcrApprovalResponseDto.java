package com.hitech.dms.web.model.pcr;

import lombok.Data;

@Data
public class PcrApprovalResponseDto {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;

}
