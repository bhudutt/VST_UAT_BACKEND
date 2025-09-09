package com.hitech.dms.web.model.common.search;


import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CommonFilterFinalResponse {

	private List<Map<String, Object>> hoObject;
	private List<Map<String, Object>> zoneObject;
	private List<Map<String, Object>>  stateObject;
	private List<Map<String, Object>>  territoryObject;
	private List<Map<String, Object>> dealeroObject ;
	private List<Map<String,Object>> branchList;
	private Integer statusCode;
	private String message;
	
	

	
	
}
