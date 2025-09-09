package com.hitech.dms.web.model.activityplan.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ActivityPlanViewResponseModel {

	private String activityPlanNo;
	private String activityPlanStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date activityPlanDate;
	private String state;
	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
	private String activityType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date toDate;
	private Integer totalnumberofDays;
	private BigDecimal totalActivityCost;
	private BigInteger vstShare;
	private BigInteger numberOfJobCardTarget;
	private BigInteger serviceRevenueTarget;
	private BigInteger numberOfEnquiry;
	private BigInteger numberofDelivery;
	private Integer activityDtlId;
}
