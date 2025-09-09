package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SpareCustOrderSearchListResponseModel {
	
	
	private Integer id;
	
	private String action;
	
//	private String customerOrderPick;
	
	private String customerOrderNumber;
	
	private String customerOrderStatus;
	
	private BigInteger branchId; 
	
	private String partyType;
	
	private String partyCode;
	
	private String productCategory; 
	
//	private Integer productCategoryId; 
	
	private String address;
	
	private String tehsil;
	
	private String pinCode;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date customerOrderDate;
	
	private String state;
	
	private String city;
	
	private BigInteger totalPart;
	
//	private String partName;
	
	private String district;
	
	private String postOffice;
	
	private BigInteger totalQty;
	
	private String partyName;
	
	private String productSubCategory;
		
	

}
