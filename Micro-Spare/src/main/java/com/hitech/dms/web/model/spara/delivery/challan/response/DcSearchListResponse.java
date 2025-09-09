package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.util.List;

import lombok.Data;

@Data
public class DcSearchListResponse {
	
	private List<DcSearchBean> searchList;
	
	private int statusCode;
	
	private Integer totalRowCount;
	
	private Integer rowCount;
	
	private String msg;

}
