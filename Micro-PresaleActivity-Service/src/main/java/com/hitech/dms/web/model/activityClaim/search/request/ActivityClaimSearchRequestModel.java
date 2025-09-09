package com.hitech.dms.web.model.activityClaim.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String activityClaimNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private Integer page;
	private Integer size;

}
