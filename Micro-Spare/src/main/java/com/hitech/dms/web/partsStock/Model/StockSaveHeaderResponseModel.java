package com.hitech.dms.web.partsStock.Model;

import java.util.List;

import lombok.Data;

@Data
public class StockSaveHeaderResponseModel {
	
	private int branchId;
	private List<StockHeaderWithPartBranchIdModel> stockHeaderList;
	private String message;
	private int statusCode;
	private int partBranchId;

}
