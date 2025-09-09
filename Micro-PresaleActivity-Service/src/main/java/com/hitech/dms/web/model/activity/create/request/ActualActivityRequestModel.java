/**
 * 
 */
package com.hitech.dms.web.model.activity.create.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.app.utils.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActualActivityRequestModel {
	private BigInteger dealerId;
	private BigInteger activityPlanHdrId;
	private BigInteger activityId;
	@JsonDeserialize(using = DateHandler.class)
	private Date activityActualDate;
	private String activityLocation;
	@JsonDeserialize(using = DateHandler.class)
	private Date activityActualFromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date activityActualToDate;
	private String activityActualStatus;
	private List<ActualActivityForENQRequestModel> enquiryList;
}
