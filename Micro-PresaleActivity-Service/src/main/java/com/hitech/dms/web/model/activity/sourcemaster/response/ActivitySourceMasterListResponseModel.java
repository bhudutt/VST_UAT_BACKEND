package com.hitech.dms.web.model.activity.sourcemaster.response;

import lombok.Data;

@Data
public class ActivitySourceMasterListResponseModel {
	private String ActivityType;
	private String ProfitCenter;
	private String activityCode;
	private String GlCode;
	private String ActivitySourceName;
	private Double ActivityCostPerDay;
	private Character IsActive;
	private Integer id;
	private Integer profitCenterId;
}

