package com.hitech.dms.web.model.baymaster.create.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BayMasterRequest {

	private String bayCode;
	private int bayType;
	private String bayDesc;
	private int dealerId;

	@JsonProperty
	private boolean isActive;


}
