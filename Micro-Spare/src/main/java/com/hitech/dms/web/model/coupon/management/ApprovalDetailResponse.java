package com.hitech.dms.web.model.coupon.management;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ApprovalDetailResponse {
	
	
	private BigInteger userId;
	private Integer usertypeId;
	private BigInteger hoUserId;
	private String employeeName;
	private String employeeCode;
	private Integer departmentId;
	private Integer hoDesignationId;
	private Integer hoDesignationLevelId;
	private int statusCode;
	private String statusMessage;
}
