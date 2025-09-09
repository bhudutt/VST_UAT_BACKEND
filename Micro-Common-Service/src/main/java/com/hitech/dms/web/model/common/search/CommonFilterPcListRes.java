package com.hitech.dms.web.model.common.search;

import java.util.List;

import lombok.Data;

@Data
public class CommonFilterPcListRes {
	
	
	private List<PcListCommon> pcList;
	private String message;
	private Integer statusCode;
	


}
