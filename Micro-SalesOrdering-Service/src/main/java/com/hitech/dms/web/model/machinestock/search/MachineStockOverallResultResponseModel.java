package com.hitech.dms.web.model.machinestock.search;

import java.util.List;

import lombok.Data;

@Data
public class MachineStockOverallResultResponseModel {

	private List<MachineStockOverAllSearchResponseModel> searchResult;
	private Integer recordCount;
}
