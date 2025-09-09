package com.hitech.dms.web.partsStock.Model;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PartsStockUploadModel {
	
	
	@JsonProperty(value = "partNo",required = true)
	private String partNo;
	
	private int partId;
	
	@JsonProperty(value = "store",required = true)
	private String store;
	
	@JsonProperty(value = "storeBinLocation",required = true)
	private String storeBinLocation;
	
	@JsonProperty(value = "quantity",required = true)
	private int quantity;
	
	@JsonProperty(value = "mrpPrice",required = true)
	private float mrpPrice;
	
	@JsonProperty(value = "ndpPrice",required = true)
	private float ndpPrice;
	
	@JsonProperty(value = "branch",required = true)
	private int branch;
	
	@JsonProperty(value = "dealer",required = true)
	private int dealer;
	
}
