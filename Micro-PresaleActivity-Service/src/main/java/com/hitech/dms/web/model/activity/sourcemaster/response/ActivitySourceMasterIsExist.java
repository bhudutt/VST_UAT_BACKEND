package com.hitech.dms.web.model.activity.sourcemaster.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivitySourceMasterIsExist {
	
	private Integer totalCount;
	
	private BigDecimal costPerDay;
	
	private  String activityType;
	
}
