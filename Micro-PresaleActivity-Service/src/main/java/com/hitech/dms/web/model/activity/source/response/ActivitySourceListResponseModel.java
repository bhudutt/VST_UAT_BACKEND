package com.hitech.dms.web.model.activity.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
//@JsonInclude(Include.NON_NULL)
public class ActivitySourceListResponseModel {
	private Integer activityId;
	private String activityCode;
	private String activityDesc;
	private String glCode;
	private Double costPerDay;
	private Boolean isActive;
}
