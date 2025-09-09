package com.hitech.dms.web.model.activityplan.request;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ActivityPlanSearchRequestModel {

	private String activityPlanNo;
	private BigInteger hoUserId;
	private String fromDate;
	private String toDate;
	private String activityPlanStatus;
	private Integer page;
	private Integer size;
}
