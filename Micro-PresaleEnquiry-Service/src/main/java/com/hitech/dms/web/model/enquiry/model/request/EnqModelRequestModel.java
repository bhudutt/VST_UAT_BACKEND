/**
 * 
 */
package com.hitech.dms.web.model.enquiry.model.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnqModelRequestModel {
	private Integer pcId;
	private Long activityPlanID;
	private String searchText;
	private Long activityId;
	private Long dealerId;
}
