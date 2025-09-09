package com.hitech.dms.web.model.spara.customer.order.picklist.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SpareCustOrderForPickListResponse {
	
	
    private Integer id;
	
	private String customerOrderNumber;
	
	private String custOrderStatus;
	
	private BigInteger branchId; 
	
	private String branchName;
	
	private String partyCategoryName;
	
	private String partyCode;
	
	private String po_Category_Desc; 
	
	private Integer productCategoryId; 
	
	private Date customDate;
	
	private String city;
	
	private BigInteger mobileNo;
	
	private List<SpareCustOrderPartForPickListResponse> partDetailList;
	
    private Integer statusCode;
	
	private String msg;
	
	

}
