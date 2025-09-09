package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SpareCustOrderViewListResponseModel {
	
	
    private Integer id;
	
	private String customerOrderNumber;
	
	private String customerOrderStatus;
	
	private BigInteger branchId; 
	
	private String branchName;
	
	private String partyCategoryName;
	
	private String partyCode;
	
	private String po_Category_Desc; 
	
	private Integer productCategoryId; 
	
	private String address;
	
	private String tehsil;
	
	private BigInteger pinId;
	
	private String pinCode;
	
	private Date customDate;
	
	private String state;
	
	private String city;
	
	private BigInteger totalPart;
	
	private String partName;
	
	private String district;
	
	private String postOffice;
	
	private BigInteger totalQty;
	
	private String partyName;
	
    private BigDecimal baseAmount;
	
	private BigDecimal totalGSTAmount;
	
	private BigDecimal totalOrderAmount;
	
	private String mobileNo;
	
	private Boolean isPickListCreated;
	
	private Integer partyTypeId;
	
	private BigInteger branchPartyCodeId;
	
	private List<SpareCustOrderPartDetailResponse> partDetailList;
	
	
    private Integer statusCode;
	
	private String msg;
	
	

}
