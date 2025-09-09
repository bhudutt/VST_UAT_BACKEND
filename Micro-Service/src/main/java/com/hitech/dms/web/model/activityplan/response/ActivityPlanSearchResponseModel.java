package com.hitech.dms.web.model.activityplan.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ActivityPlanSearchResponseModel {
	
    private Integer id;
    private String action;
	private String activityPlanNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date activityPlanDate;
	private String activityPlanStatus;
	private String dealerCode;
	private String activityType;
	private String dealerName;
	private String dealerLocation;
	private String state;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date fromDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date toDate;
	private Integer totalNumberOfDays;
	private BigDecimal totalActivityCost;
	private BigInteger vstShare;
	private BigInteger numberOfJobCardTarget;
	private BigInteger serviceRevenueTarget;
	private BigInteger numberOfEnquiry;
	private BigInteger numberOfDelivery;
	
	
}
