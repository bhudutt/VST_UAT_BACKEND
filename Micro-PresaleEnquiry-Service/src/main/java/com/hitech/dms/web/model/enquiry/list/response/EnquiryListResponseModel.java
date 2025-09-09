/**
 * 
 */
package com.hitech.dms.web.model.enquiry.list.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
//@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"totalRecords"})
public class EnquiryListResponseModel {
	
	private BigInteger srNo;
	private String action;
	private String enqNumber;
	private String customerName;
	private String customerMobileNo;
	private String salesman;
	private String expectedPurchaseDate;
	private String sourceOfEnq;
	private String enqStatus;
	private String modelName;
	private String fieldActivityType;
	private String isEnqValidated;
	private String enqFrom;
	private String prospectType;
	private String enqStage;
	private String enqDate;
	private String nextFollowupDate;
	private String nextFollowupActivity;
	private String pincode;
	private String village;
	private String tehsil;
	private String subsidy;
	private String dealership;
	private String branchCode;
	private String branchName;
	private String branchLocation;
	private String vstExecutive;
	private String profitCenter;
	private String series;
	private String segment;
	private String variant;
	private String itemNo;
	private String itemDesc;
	private String activityPlanDate;
	private String activityPlanNumber;
	private String digitalPlatForm;
//	@JsonProperty(value = "Digital Validated Status")
	private Boolean digitalValidatedStatus;
//	@JsonProperty(value = "Digital Validated By")
	private String digitalValidatedBy;
//	@JsonProperty(value = "Digital Validated Date")
	private String digitalValidatedDate;
	private String customerType;
	private String customerCode;
	private String companyName;
	private String address1;
	private String address2;
	private String address3;
	private String district;
	private String state;
	private String country;
	private String cashLoan;
	private String financierName;
	private String financierLoggedDate;
	private BigDecimal loanAmountApplied;
	private Integer tenureMonth;
	private Double rateOfInterest;
	private BigDecimal emiAmount;
	private String retailFinanceStatus;
	private String disbursedDate;
	private BigDecimal disbursedAmount;
	private BigDecimal subsidyAmount;
	private String exchangeRequired;
	private String exchangeBrand;
	private String exchangeModel;
	private String exchangeYear;
	private BigDecimal exchangeAmount;
	private BigDecimal dealValue;
	private BigDecimal totalAmountReceived;
	private String remarks;
	private String subSource;
	private BigInteger enquiryId;
	private Integer totalRecords;
	
}
