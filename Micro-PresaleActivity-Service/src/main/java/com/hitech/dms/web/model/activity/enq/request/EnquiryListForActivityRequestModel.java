/**
 * 
 */
package com.hitech.dms.web.model.activity.enq.request;

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
public class EnquiryListForActivityRequestModel {
	private BigInteger dealerId;
	private BigInteger actvityPlanHDRId;
	private BigInteger actvityId;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private BigInteger actualActivityHDRId;
	private String isFor;
}
