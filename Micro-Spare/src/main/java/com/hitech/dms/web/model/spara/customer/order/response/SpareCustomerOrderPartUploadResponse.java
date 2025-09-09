package com.hitech.dms.web.model.spara.customer.order.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SpareCustomerOrderPartUploadResponse {
	
	
	private Map<String, String> errorPartData;
	private Map<String, Integer> partAndQty;
	private List<SpareCustOrderPartDetailResponse> partForExcelList;
	private String msg;
	private Integer statusCode;

}
