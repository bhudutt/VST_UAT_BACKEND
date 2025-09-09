package com.hitech.dms.web.model.activityplan.request;

import lombok.Data;

@Data
public class ActivityPlanEditModel {

	private Integer activityDtlId;
	private String fromDate;
	private String toDate;
	private Integer totalNoOfDays;
	private String totalActivityCost;
	private Integer vstShare;
	private Integer noofDayJobCardTarget;
	private Integer serviceRevenueTarget;
	private Integer noofEqquiry;
	private Integer noofDelivery;
	private Integer branchId;	
}
