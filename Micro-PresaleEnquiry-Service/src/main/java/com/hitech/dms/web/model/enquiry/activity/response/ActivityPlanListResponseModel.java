/**
 * 
 */
package com.hitech.dms.web.model.enquiry.activity.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanListResponseModel {
	private BigInteger activityPlanID;
	private String activityPlanNumber;
	private String displayValue; 
}
