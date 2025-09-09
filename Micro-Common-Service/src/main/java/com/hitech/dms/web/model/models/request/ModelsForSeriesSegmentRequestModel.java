/**
 * 
 */
package com.hitech.dms.web.model.models.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ModelsForSeriesSegmentRequestModel {
	private String seriesName;
	private String segment;
	private Long pcId;
}
