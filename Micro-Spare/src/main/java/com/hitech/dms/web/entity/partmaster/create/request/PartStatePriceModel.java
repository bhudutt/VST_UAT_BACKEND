package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartStatePriceModel {

	private String stateName;
	private String stateCode;
	private BigDecimal dealerNDP;
	private BigDecimal distrubutorNDP;
	private BigDecimal dealerMRP;
	private BigDecimal distrubutorMRP;

	private String effectiveFrom;
	private String effectiveTo;
	private String createdDate;
	private String status;
}
