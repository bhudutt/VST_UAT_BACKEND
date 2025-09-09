package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DeliveryChallanHeaderAndDetailResponse {
	
	private BigInteger dChallanId;
	private String dcNumber;
	private String dcStatus;
	private BigInteger branchId;
	private String branchName;
	private String partyType;
	private String partyCode;
	private String productCategory;
	private Integer productCategoryId;
	private String address;
	private String tahsilDesc;
	private String pinCode;
	private Date dcDate;
	private String stateDesc;
	private String cityDesc;
	private String partyName;
	private String districtDesc;
	private String postOffice;
	private BigInteger totalQty;
	private String partName;
	private BigInteger partyBranchId;
	private BigInteger totalPart;
	private BigDecimal totalValue;
	private BigDecimal totalIssuedQty;
	
	
	List<DCcustomerOrderResponse> customerOrderList = new ArrayList<>();
	
	List<DCpartDetailListResponse> detailList= new ArrayList<>();
	

}
