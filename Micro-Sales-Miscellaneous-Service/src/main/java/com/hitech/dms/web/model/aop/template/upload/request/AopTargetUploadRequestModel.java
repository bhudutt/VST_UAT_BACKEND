/**
 * 
 */
package com.hitech.dms.web.model.aop.template.upload.request;

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
public class AopTargetUploadRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	private BigInteger dealerId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "finYear", required = true)
	private String finYear;
	private int lastFinYearCount;
	@JsonProperty(value = "aopDate", required = true)
	@JsonDeserialize(using = DateHandler.class)
	private Date aopDate;
	private String remarks;
	private BigInteger aopId;
}
