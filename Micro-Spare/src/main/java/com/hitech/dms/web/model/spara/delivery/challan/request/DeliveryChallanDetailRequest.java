package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



@Data
public class DeliveryChallanDetailRequest {
	
	
	private BigInteger dchallanId;
	
	private BigInteger branchId;
	
	private Integer challanType;
	
	private String documentType;
	
	private BigInteger partyType;
	
	private String dcNumber;
	
	private Date dcDate;
	
	private String dcStatus;
	
	private BigInteger empId;
	
	private String categoryCode;
	
	private String partyCode;
	
	private String customerName;
	
	private String partyAddline1;
	
	private String partyAddline2;
	
	private String partyAddline3;
	
	private Integer cityId;
	
	private Integer pinId;
	
	private String remarks;
	
	private BigInteger totalIssuedQty;
	
	private BigDecimal totalReturnedValue;
	
	private BigDecimal totalValue;
	
	private String mobileNo;
	
	private BigDecimal baseAmount;
	
	private BigDecimal totalGstAmount;
	
	private BigDecimal totalOrderAmount;
	
    private Date createdDate;
	
	private BigInteger createdBy;
	
	private Date modifiedDate;
	
	private BigInteger modifiedBy;
	@JsonProperty(value="dcSelectList")
	private List<DcSelectCustomerOrderRequest> dcSelectCO = new ArrayList<>();
	
	//Details
	@JsonProperty(value="sparePartDetails")
	List<DeliveryChallanPartDetailRequest> dcPartDetailList = new ArrayList<>();

}
