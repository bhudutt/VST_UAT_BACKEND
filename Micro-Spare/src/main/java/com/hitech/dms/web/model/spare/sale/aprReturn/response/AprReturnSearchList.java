package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class AprReturnSearchList {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AprReturnSearchRep> searchList=new LinkedList<AprReturnSearchRep>();
	
	private int statusCode;
	
	private Integer totalRowCount;
	
	private Integer rowCount;
	
	private String msg;
	

}
