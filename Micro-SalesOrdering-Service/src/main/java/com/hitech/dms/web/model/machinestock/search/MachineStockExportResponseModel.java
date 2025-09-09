package com.hitech.dms.web.model.machinestock.search;

import lombok.Data;

@Data
public class MachineStockExportResponseModel {

	private String msg;
	private String fileName;
	private boolean isFileCreated;
}
