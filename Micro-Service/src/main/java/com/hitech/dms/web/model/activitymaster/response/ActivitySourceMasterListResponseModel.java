package com.hitech.dms.web.model.activitymaster.response;




import lombok.Data;

@Data
public class ActivitySourceMasterListResponseModel {

	private Integer id;
	private String activityType;
	private String profitCenter;
	private String glCode;
	private String activitySourceName;
	private String activityCostPerDay;
	private Character active;
}
