package com.hitech.dms.web.model.retailFollowUp.create.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpEnquiryDtlResponse {

private BigInteger enquiryId;
private BigInteger branchId;
private String branchName;
private String enquiryNumber;
private String enquiryDate;
private String enquiryStatus;
private String customerName;
private String mobileNo;
private String profitCenter;
private String model;
private String variant;
private String series;
private String segment;
private String itemNo;
private String itemDesc;
private Integer enquiryStageId;
private String financierName;
private String cashOrLoan;
private BigInteger financierPartyId;
private String retailFinaceStatus;
private BigInteger  retailFollowupHdrId;
private String loanStatus;
private BigInteger salesmanId;
private Date disbursementDate;
private BigDecimal disbursementAmount;

}
