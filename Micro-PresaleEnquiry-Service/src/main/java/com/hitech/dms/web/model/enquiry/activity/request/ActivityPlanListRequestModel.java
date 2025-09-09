/**
 * 
 */
package com.hitech.dms.web.model.enquiry.activity.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanListRequestModel {
	private String userCode;
	private BigInteger dealerID;
	private Integer pcID;
	private Integer activityID;
	@JsonDeserialize(using = DateHandler.class)
	private Date fieldActivityDate;
}
