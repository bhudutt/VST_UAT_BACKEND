package com.hitech.dms.web.model.machinestock.search;

import java.util.List;

import lombok.Data;

@Data
public class MachineStockSearchResultResponseModel {
	private List<MachineStockSearchResponseModel> searchResult;
	private Integer recordCount;
}
