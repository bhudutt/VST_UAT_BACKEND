package com.hitech.dms.web.model.storemaster.create.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonIgnoreProperties({"msg","statusCode"})
@JsonPropertyOrder({"storeCode","storeDesc","active","defauleStore"})
public class StroeSearchResponseModel {

	private Integer storeId;
	@JsonProperty("Store Code")
	private String storeCode;
	@JsonProperty("Store Descripton")
	private String storeDesc;
	
	private String active;
	@JsonProperty("Default Store")
	private String defauleStore;
	private String msg;
	private Integer statusCode;
}
