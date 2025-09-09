/**
 * 
 */
package com.hitech.dms.web.model.activity.source.form.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityFormRequestModel {
	private Integer pcId;
	private String activityCode;
	private String activityDescription;
	private String glCode;
	private Double costPerDay;
	private Boolean isActive;
}
