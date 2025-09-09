package com.hitech.dms.web.model.activityplan.search.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
@JsonPropertyOrder({"srlNo","action","profitCenter","state","dealerName","dealerCode","activityNumber","activityPlanStatus","activityMonth","segment","financialYear","billingPlan","totalBudget","totalDays"})
@JsonIgnoreProperties({"series","billingPlan"})
public class ActivityPlanSearchResponseModel {
	private BigInteger srlNo;
	private String action;
	private BigInteger activityPlanHdrId;
	private String activityNumber;
	private String activityPlanStatus;
	@JsonProperty("Plan Upload Date")
	private String activityDate;
	private String profitCenter;
	private String series;
	private String segment;
	private String financialYear;
	private String activityMonth;
	private String state;
	private String dealerName;
	private String dealerCode;
	private String billingPlan;
	private String totalBudget;
	private String totalDays;
	@JsonProperty("Plan Uploader Name")
	private String planUploaderName;
	private String stateHeadRemark;
	private String mktDivHeadRemark;
	private String levelOneApprovedDate;
	private String levelTwoApprovedDate;
	
}
