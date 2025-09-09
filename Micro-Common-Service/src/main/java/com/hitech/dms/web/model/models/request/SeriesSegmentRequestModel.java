/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.models.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class SeriesSegmentRequestModel {
	private BigInteger pcId;
	private String isFor;
	
}
