package com.hitech.dms.web.model.activity.dtl.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ActualActivityDTLResponseModel {
	private BigInteger dealerId;
	private String dealerName;
	private Integer pcId;
	private String pcDesc;
	private String activityNo;
	private String activityCreationDate;
	private Integer activityMonthNo;
	private String activityMonth;
	private int activityYear;
	private BigInteger activityPlanHdrId;
	private Integer activityId;
	private String activityName;
	private String actualActivityNo;
//	private BigInteger actualActivityHDRId;
	private String activityLocation;
	private String activityActualFromDate;
	private String activityActualToDate;
	private String activityActualStatus;
}
