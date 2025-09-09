/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityPlanModelResponse {
	private String activityType;
	private String activityPlan;
	private Date activityPlanDate;
}
