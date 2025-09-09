package com.hitech.dms.web.model.vehicle.master;

import lombok.Data;

@Data
public class VehicleMasterRequestModel {

	private String chassisNo;
	private String engineNo;
	private String registrationNo;
	private String customerCode;
	private String customerName;
	private String mobileNo;
	private Integer branchId;
	private Integer page;
	private Integer size;

}
