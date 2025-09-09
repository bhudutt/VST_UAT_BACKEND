/**
 * 
 */
package com.hitech.dms.web.model.productModels;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ModelsForSeriesSegmentResponseModel {
	private BigInteger modelId;
	private String modelName;
	private String series;
	private String segment;
}
