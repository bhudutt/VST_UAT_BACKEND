/**
 * 
 */
package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
//@JsonInclude(Include.NON_NULL)
public class EnquiryViewResponseModel {
	private BigInteger enquiryId;
	private BigInteger branchId;
	private String branchName;
	private String enqNumber;
	private String enquiryDate;
	private String enquiryStatus;
	private String sourceOfEnquiry;
	private String profitCenter;
	private Integer pcId;
	private BigInteger enquiryTypeId;
	private Integer enquiryStageId;
	private Integer enquirySourceId;
	private Integer activitySourceID;
	private String enquiryType;
	private String expectedPurchaseDate;
	private String nextFollowupDate;
	private BigInteger salesmanId;
	private String dspName;
	private String enquiryRemarks;
	private String activityDesc;
	private String digitalSourceName;
	private String customerName;
	private String prospectType;
	private String customerCode;
	private BigInteger customerId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private BigInteger pinId;
	private BigInteger cityId;
	private BigInteger tehsilId;
	private BigInteger districtId;
	private BigInteger stateId;
	private BigInteger countryId;
	private String enquiryStage;
	private String financierName;
	private String nextFollowupActivity;
	private BigInteger nextFollowupActivityId;
	private String validationDate;
	private String validationRemarks;
	private String fieldActivityType;
	private BigInteger activityPlanHDRID;
	private String activityDate;
	private String activityPlanNo;
	private String digitalPlatform;
	private String digitalEnqDate;
	private Integer digitalSourceId;
	private Boolean digitalValidationStatus;
	private String digitalValidationBy;
	private String mobileNo;
	private String prospectCode;
	private String prospectCategory;
	private BigInteger customerCategoryId;
	private String companyName;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String whatsappNo;
	private String alternateNo;
	private String phoneNumber;
	private String emailId;
	private String address1;
	private String address2;
	private String address3;
//	private String pinCode;
//	private String village;
//	private String tehsil;
//	private String district;
//	private String country;
	private String dateOfBirth;
	private String anniversaryDate;
	private String panNo;
	private String gstIn;
	private BigInteger machine_item_id;
	private String itemNo;
	private String itemDescription;
	private BigInteger modelId;
	private String modelName;
	private String variant;
	private String seriesName;
	private String segmentName;
	private Integer enq_close_brand_id;
	private BigInteger digital_Enq_DTL_ID;
	
	
	private String machine_received;
	private BigInteger occupationID;
	private String occupation;
	private Double landSize;
	private BigDecimal dealPrice;
	
	private String cashOrLoan;
	private BigInteger financier_party_id;
	private String financier;
	private String financeLogDate;
	private BigDecimal loanAmnt;
	private Integer tenure;
	private Double annualRate;
	private BigDecimal emiAmnt;
	private String retailFinaceStatus;
	private String disbursedDate;
	private BigDecimal disbursedAmt;
	private BigDecimal ActualLoanAmt;
	private BigDecimal pendingLoanAmt;
	
	
	private BigDecimal actualAmtReceived;
	private BigDecimal marginAmt;
	private BigDecimal initialMarginAmt;
	private BigDecimal remainingMarginAmt;
	private BigDecimal pendingMarginAmt;
	private BigDecimal PendingMarginAmt;
	
	
	
	
	
	
	
	private BigDecimal estimated_exchange_price;
	private BigDecimal actualExchangeAmt;
	private BigDecimal pendingExchangeAmt;
	private Boolean exchangeRequired;
	private BigInteger exchangeBrandId;
	private BigInteger enquiryExcDTLId;
	private String exchangeBrand;
	private String exchangeModel;
	private Integer exchangeModel_year;
	
	private Boolean subsidy_required;
	private BigDecimal subsidyAmt;
	private BigDecimal actualSubsidyAmt;
	private BigDecimal pendingSubsidyAmt;
	
	private String exchangeStatus;
}
