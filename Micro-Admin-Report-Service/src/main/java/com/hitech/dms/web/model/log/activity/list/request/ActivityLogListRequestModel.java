/**
 * 
 */
package com.hitech.dms.web.model.log.activity.list.request;

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
public class ActivityLogListRequestModel {
	private BigInteger pcID;
	private BigInteger orgHierID;
	private BigInteger dealerID;
	private BigInteger branchID;
	private String userCode;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
}
