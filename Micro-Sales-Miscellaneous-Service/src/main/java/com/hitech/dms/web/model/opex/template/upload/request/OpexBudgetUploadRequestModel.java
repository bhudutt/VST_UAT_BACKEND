/**
 * 
 */
package com.hitech.dms.web.model.opex.template.upload.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class OpexBudgetUploadRequestModel {
	@JsonProperty(value = "stateId", required = true)
	private BigInteger stateId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "finYear", required = true)
	private String finYear;
	private int lastFinYearCount;
	@JsonProperty(value = "opexDate", required = true)
	@JsonDeserialize(using = DateHandler.class)
	private Date opexDate;
	private String remarks;
	private BigInteger opexId;
}
