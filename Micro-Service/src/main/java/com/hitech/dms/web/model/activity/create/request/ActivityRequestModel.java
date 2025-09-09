package com.hitech.dms.web.model.activity.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class ActivityRequestModel {
	private BigInteger dealerId;
	private String isFor;
	private String searchText;
	
	private String activityNumber;
	private String activityDate;
	private String activityPlanNo;
	private String activityPlanDate;
	private Integer pcId;
	private String activityName;
	private String activityLocation;
	private String activityFromDate;
	private String activityToDate;
	private String actualActivityFromDate;
	private String actualActivityToDate;
	private BigDecimal totalAmount;
	private List<ActivityRequestDtlModel> detailRequest;
	private Integer page;
	private Integer size;
	private Integer branchId;
	
	
}
