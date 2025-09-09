package com.hitech.dms.web.model.machinestock.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Data
public class MachineStockListRequestModel {

	private String userCode;
	private String dealerCode;
	@JsonDeserialize(using = DateHandler.class)
	private String fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private String toDate;
	private String chassisNo;
	private String engineNo;
	private String vinNo;
	private String invoiceNo;

}
