/**
 * 
 */
package com.hitech.dms.web.model.activityplan.dtl.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanHDRResponseModel {
	private BigInteger activityPlanHdrId;
	private Integer pcId;
	private String pcDesc;
	private String activityNo;
	private String activityCreationDate;
	private int activityMonth;
	private String activityMonthName;
	private int activityYear;
	private String activityStatus;
	private String seriesName;
	private String segmentName;
}
