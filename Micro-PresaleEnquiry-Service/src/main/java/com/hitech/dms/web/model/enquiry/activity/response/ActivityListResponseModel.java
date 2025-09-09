/**
 * 
 */
package com.hitech.dms.web.model.enquiry.activity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ActivityListResponseModel {
	private Integer activityId;
	private String activityCode;
	private String activityName;
	private String glCode;
	private Double costPerDay;
}
