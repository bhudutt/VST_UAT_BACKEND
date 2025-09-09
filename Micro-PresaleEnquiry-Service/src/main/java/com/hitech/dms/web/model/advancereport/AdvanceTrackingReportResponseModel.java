package com.hitech.dms.web.model.advancereport;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AdvanceTrackingReportResponseModel {
	
	@JsonProperty("state")
	private String state;
	@JsonProperty("cluster")
    private String cluster;
	@JsonProperty("tm")
    private String tm;
	@JsonProperty("dealershipName")
    private String dealershipName;
	@JsonProperty("location")
    private String location;
	@JsonProperty("Enquiry No")
	private String enquiryNo;
	 @JsonProperty("customerName")
    private String customerName;
	 @JsonProperty("village")
    private String village;
	 @JsonProperty("tehsil")
    private String tehsil;
	 @JsonProperty("district")
    private String district;
	 
    @JsonProperty("customerMobileNo")
    private String contactNo;
    @JsonProperty("dateOfDelivery")
    private String dateOfDelivery;
    @JsonProperty("model")
    private String model;
    @JsonProperty("chassis")
    private String chassis;
    @JsonProperty("ageing")
    private Integer ageing;
    @JsonProperty("ndpPrice")
    private BigDecimal ndpPrice;
    @JsonProperty("dealPrice")
    private BigDecimal dealPrice;
    @JsonProperty("loanOrCash")
    private String loanOrCash;
    @JsonProperty("financialInstitution")
    private String financialInstitution;
    @JsonProperty("loanAmount")
    private BigDecimal loanAmount;
    @JsonProperty("loanAmountDisbursed")
    private Integer loanAmountDisbursed;
 //   private BigDecimal balanceOwed;
    @JsonProperty("dateOfDisbursement")
    private String dateOfDisbursement;
    @JsonProperty("retailFinanceStage")
    private String retailFinanceStage;
    @JsonProperty("margin money received")
    private BigDecimal marginMoneyReceivedAmount;
    @JsonProperty("subsidyEstimatedAmount")
    private  BigDecimal subsidyEstimatedAmount;
    @JsonProperty("exchange")
    private Character exchange;
    @JsonProperty("exchangePurchaseValue")
    private String exchangePurchaseValue;
    @JsonProperty("exchangeSoldValue")
    private BigDecimal exchangeSoldValue;
    @JsonProperty("totalAmountRecieved")
    private BigDecimal totalAmountRecieved;
    @JsonProperty("Balance O/s amt")
    private BigDecimal pendingRetailAmount;
    @JsonProperty("expectedDateOfRetail")
    private String expectedDateOfRetail;
    @JsonProperty("retailStatus")
    private String retailStatus;
    @JsonProperty("remarks1")
    private String remarks1;
    @JsonProperty("remarks2")
    private String remarks2;
    @JsonProperty("remarks3")
    private String remarks3;
    private BigInteger enquiryId;
	/*
	 * @JsonProperty("subStage") private String subStage;
	 */
    
    
    // new column addes ramk 
    
  
  //  private Integer loanAmountDisbursed;
   
	/*
	 * @JsonProperty("isExchangeTractorAvailable") private char
	 * isExchangeTractorAvailable;
	 */
    
    
  //  private BigDecimal exchangeSaleableValue;
    
    
    
	/*
	 * @JsonProperty("preApproval") private String preApproval;
	 * 
	 * @JsonProperty("postApproval") private String postApproval;
	 */
    
    
    

    
    
}
