/**
 * 
 */
package com.hitech.dms.web.model.activity.search.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActualActivitySearchResponse {
	private BigInteger srlNo;
	private BigInteger activityActualHdrId;
	private String activityActualNo;
	private String activityActualDate;
	private String activityDesc;
	private String activityLocation;
	private String activityActualFromDate;
	private String activityActualToDate;
	private String activityActualStatus;
}
