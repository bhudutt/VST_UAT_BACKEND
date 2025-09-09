package com.hitech.dms.web.model.master.response;

import lombok.Data;

@Data
public class PlantMasterModel {

	private String plantName; 
	private String plantCode; 
	private String msg;
	private Integer statusCode;
}
