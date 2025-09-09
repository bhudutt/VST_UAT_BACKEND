/**
 * 
 */
package com.hitech.dms.web.model.productModels;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ModelsForSeriesSegmentRequestModel {
	private String seriesName;
	private String segment;
	private Integer pcId;
}
