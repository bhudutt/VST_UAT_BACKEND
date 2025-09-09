package com.hitech.dms.web.model.vehicle.master;

import java.util.List;

import lombok.Data;

@Data
public class VehicleMasterSearchListResultResponseModel {

	private List<VehicleMasterSearchListResponseModel> searchResult;
	private Integer recordCount;
}
