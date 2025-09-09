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
public class ModelItemListRequestModel {
	private Integer pcID;
	private Long activityPlanID;
	private Long modelID;
	private String searchValue;
	private Long activityId;
	private Long dealerId;
	private String isFor;
}
