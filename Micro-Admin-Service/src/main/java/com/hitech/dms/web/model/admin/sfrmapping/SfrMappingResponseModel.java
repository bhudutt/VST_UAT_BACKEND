package com.hitech.dms.web.model.admin.sfrmapping;

import org.json.simple.JSONArray;

import lombok.Data;

@Data
public class SfrMappingResponseModel {

	private String msg;
	private Integer statusCode;
	private JSONArray stockJsonArr;
	private String aprNumber;
}
