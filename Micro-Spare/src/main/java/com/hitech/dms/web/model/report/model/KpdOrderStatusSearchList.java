package com.hitech.dms.web.model.report.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class KpdOrderStatusSearchList implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<KpdOrderStatusSearchResponse> searchList = new LinkedList<KpdOrderStatusSearchResponse>();

	private int statusCode;
	
	private Integer totalRowCount;
	
	private Integer rowCount;
	
	private String msg;
}
