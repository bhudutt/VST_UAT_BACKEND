/**
 * 
 */
package com.hitech.dms.web.model.activityplan.upload.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanUploadRequestModel {
	private Integer pcID;
	private String seriesName;
	private String segmentName;
	private Integer month;
	private Integer year;
	private String isInactiveInclude;
}
