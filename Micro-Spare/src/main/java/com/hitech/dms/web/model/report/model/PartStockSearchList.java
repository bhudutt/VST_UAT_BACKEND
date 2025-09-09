package com.hitech.dms.web.model.report.model;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
@Data
public class PartStockSearchList {
	

	private List<PartStockSearchRes> searchList=new LinkedList<PartStockSearchRes>();
	
	private int statusCode;
	
	private Integer totalRowCount;
	
	private Integer rowCount;
	
	private String msg;
	

}
