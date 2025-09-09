package com.hitech.dms.web.model.activity.source.response;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivitySourceListResponseModel {
	private Integer activityId;
	private String activityCode;
	private String activityDesc;
	private String glCode;
	private Double costPerDay;
	private Boolean isActive;
}
