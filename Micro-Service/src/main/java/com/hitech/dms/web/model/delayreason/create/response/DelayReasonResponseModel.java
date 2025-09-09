package com.hitech.dms.web.model.delayreason.create.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties({"reasonId","msg","statusCode","recordCount","status"})
public class DelayReasonResponseModel {

	private Integer reasonId;
	private String reasonCode;
	private String reasonDesc;
	private Boolean status;
	@JsonProperty("Status")
	private String statusVal;
	private String msg;
	private Integer statusCode;
	private Integer recordCount;
}
