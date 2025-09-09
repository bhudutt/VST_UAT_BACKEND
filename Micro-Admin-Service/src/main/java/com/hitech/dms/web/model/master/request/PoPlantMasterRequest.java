package com.hitech.dms.web.model.master.request;

import lombok.Data;

@Data
public class PoPlantMasterRequest {

	private String plantName;
	private String plantCode;
	private String pinCode;
	private String location;
}
