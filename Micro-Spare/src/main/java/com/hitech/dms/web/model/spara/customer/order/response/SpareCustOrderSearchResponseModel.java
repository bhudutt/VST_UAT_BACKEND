package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class SpareCustOrderSearchResponseModel {
	
	
	List<SpareCustOrderSearchListResponseModel> detailList;
	
	private Integer rowCount;
	
	private Integer totalRowCount;
	
	private Integer statusCode;
	
	private String msg;
	
	




}
