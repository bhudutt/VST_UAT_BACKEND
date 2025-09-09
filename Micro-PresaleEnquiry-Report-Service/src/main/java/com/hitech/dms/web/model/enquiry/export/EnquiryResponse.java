package com.hitech.dms.web.model.enquiry.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"dealerCode","dealerName","dealerLocation","enquiryDate","enquiryNumber",
	"customerName","mobileNumber","salesman","territoryManager","prospectType","enquiryStatus",
	"modelName","sourceOfEnquiry","subSource","enqStage","isenqValidated","expectedDateOfPurchase",
	"nextFollowUpDate","village","tahsil","district","state","cash","remarks"})
public class EnquiryResponse {
	
	
	private String dealerCode;
    private String dealerName;
    private String dealerLocation;
    private String enquiryDate;
    private String enquiryNumber;
    private String modelName;
    private String isenqValidated;
    private String expectedDateOfPurchase;
    private String  prospectType;
    private String enquiryStatus;
    private String sourceOfEnquiry;
    private String  subSource;
    private String  territoryManager ;
    @JsonProperty("salesman(DSP)") 
    private String  salesman;
    private String customerName;
    private String mobileNumber;
    private String village;
    private String  tahsil;
    private String district;
    private String state;
    private String enqStage;
    private String nextFollowUpDate;
    private String cash;
    private String remarks;
    private Integer totalRecords;


}
