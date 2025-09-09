package com.hitech.dms.web.model.activity.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({"recordCount"})
public class ActivityResponseModel {

	private Integer activityHdrId;
	private String activityNo;
	private Date activityCreationDate;
	
	private Integer activityPlanId;
	private String activityPlanNumber;
	private Date activityPlanDate;
	private List<ActivityResponseModel> activityNameList;
	private Date activityFromDate;
	private Date activityToDate;
	
	private Date actualFromDate;
	private Date actualToDate;
	private Integer pcId;
	private String pcName;
	private Integer activityTypeId;
	private String activityTypeName;
	private String activityLocation;
	private BigDecimal totalAmount;
	private String activityName;
	private String branchName;
	
	private Integer statusCode;
	private String msg;
	
	
	
	
}
