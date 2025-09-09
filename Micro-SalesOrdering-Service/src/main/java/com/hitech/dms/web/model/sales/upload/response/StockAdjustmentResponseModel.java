package com.hitech.dms.web.model.sales.upload.response;

import org.json.simple.JSONArray;


import lombok.Data;

@Data
public class StockAdjustmentResponseModel {

	private String msg;
	private Integer statusCode;
	private JSONArray stockJsonArr;
	private String stockNumber;
}
