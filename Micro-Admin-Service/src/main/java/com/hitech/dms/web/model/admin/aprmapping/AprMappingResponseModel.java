package com.hitech.dms.web.model.admin.aprmapping;

import org.json.simple.JSONArray;

import lombok.Data;

@Data
public class AprMappingResponseModel {

	private String msg;
	private Integer statusCode;
	private JSONArray stockJsonArr;
	private String aprNumber;
}
