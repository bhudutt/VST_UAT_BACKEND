package com.hitech.dms.web.model.pcr;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class JobCardPcrPartDto {
	private Integer partId;
	private String PartNumber;
	private String PartDesc;
	private BigDecimal ClaimQty;
	private BigDecimal ApprovedQty;

	private Long FailureTypeId;
	private String FailureType;
	private String FailureCode;
	private String FailureDescription;

}
