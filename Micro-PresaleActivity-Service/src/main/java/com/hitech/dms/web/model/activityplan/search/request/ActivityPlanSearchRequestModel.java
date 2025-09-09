package com.hitech.dms.web.model.activityplan.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanSearchRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger orgHierID;
	private String activityNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	
	private String fromDate1;
	private String toDate1;
	
	private int page;
	private int size;
}
