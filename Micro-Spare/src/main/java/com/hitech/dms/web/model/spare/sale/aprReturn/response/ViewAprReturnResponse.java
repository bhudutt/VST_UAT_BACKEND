package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ViewAprReturnResponse {
	
	private String branchName;
	
	private String partyName;
	
	private String partycode;
	
	private String partyType;
	
	private String invoiceNumber;
	
	private String invoiceDate;
	
	private String mobileNumber;
	
	private String partyAddress;
	
	private String pinCode;
	
	private String stateName;
	
	private String districtName;
	
	private String tehsilName;
	
	private String cityName;
	
	private String postOfficeName;
	
	private String aprNumber;
	
	private String aprReturnStatus;
	
	private String aprDate;
	
	private BigDecimal totalTaxableAmount;
	
	private BigDecimal totalGstAmt;
	
	private BigDecimal returnAmount;
	
	private String remark;
	
	
	private List<ViewAprReturnDtlResponse> detailList =new ArrayList<>();

}
