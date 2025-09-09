package com.hitech.dms.web.model.activity.create.response;



import java.math.BigDecimal;
import java.util.Date;


import lombok.Data;

@Data
//@JsonIgnoreProperties({""})
public class ActivityViewResponseModel {
	
	private Integer activityHdrId;
	private String activityNo;
	private String activityCreationDate;
	private String activityPlanNumber;
	private String activityPlanDate;
	private String activityFromDate;
	private String activityToDate;
	private String actualFromDate;
	private String actualToDate;
	private String pcName;
	private String activityLocation;
	private BigDecimal totalAmount;
	private String activityName;
	private String branchName;
	
	
	
	
	

}
