package com.hitech.dms.web.model.admin.village.response;

import java.util.List;
import java.util.Map;

import com.hitech.dms.web.model.admin.village.request.VillageRequest;

import lombok.Data;

@Data
public class VillageUploadExcelRes {
	
	private Map<String, String> errorData;
	private Map<String, Integer> demography;
	private List<VillageRequest> villageForExcelList;
	private String msg;
	private Integer statusCode;

}
