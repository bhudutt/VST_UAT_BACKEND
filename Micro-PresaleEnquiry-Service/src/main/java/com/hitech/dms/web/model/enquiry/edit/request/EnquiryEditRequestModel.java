/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditRequestModel {
	// EnquiryEditMapId
	private BigInteger enquiryHdrId;

	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Branch Is Required")
	private BigInteger branchId;
	@JsonProperty(value = "pcId", required = true)
	@NotNull(message = "Profit Center Is Required")
	private BigInteger pcId;

	private String enquiryNo;

	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "enquiryDate", required = true)
	@NotNull(message = "Enquiry Date Is Required")
	private Date enquiryDate;

	private String latitude;
	private String longitude;

	@JsonProperty(value = "enquiryTypeid", required = true)
	@NotNull(message = "Enquiry Type Is Required")
	private BigInteger enquiryTypeid;
	@JsonProperty(value = "enquiryStageId", required = true)
	@NotNull(message = "Enquiry Stage Is Required")
	private Integer enquiryStageId;
	private Boolean appEnquiryFlag;

	@JsonProperty(value = "enquiryStatus", required = true)
	@NotNull(message = "Enquiry Status Is Required")
	private String enquiryStatus;
	@JsonProperty(value = "enquirySource", required = true)
	@NotNull(message = "Enquiry Source Is Required")
	private Integer enquirySource;
	private String enqRemark;

	private Integer digitalSourceId;

	@JsonDeserialize(using = DateHandler.class)
	private Date digitalEnqDate;

	private Boolean digitalValidationStatus;

	private String digitalValidationBy;

	private Integer activitySourceID;

	@JsonDeserialize(using = DateHandler.class)
	private Date activityDate;

	private BigInteger activityPlanHDRId;

	@JsonProperty(value = "salesmanId", required = true)
	@NotNull(message = "Salesman Is Required")
	private BigInteger salesmanId;

	private BigInteger customerId;

	private Float landInAcres;

	private String validationBy;
	@JsonDeserialize(using = DateHandler.class)
	private Date validationDate;
	private String validationRemarks;

	@JsonDeserialize(using = DateHandler.class)
	@NotNull(message = "Expected Purchase Date Is Required")
	private Date expectedPurchaseDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date nextFollowupDate;
	private BigInteger nextFollowupActivityId;

	@JsonProperty(value = "modelId", required = true)
	@NotNull(message = "Model Is Required")
	private BigInteger modelId;

	@JsonProperty(value = "variant", required = true)
	private String variant;

	private BigInteger machineItemId;

	private String purpose_of_purchase;

	@JsonProperty(value = "isExchangeRequired", required = true)
	@NotNull(message = "IsExchnage Is Required")
	private Boolean isExchangeRequired;
	private Boolean isExchangeReceived;

	private Float estimateExchangePrice;

	@JsonDeserialize(using = DateHandler.class)
	private Date enqCloseDate;

	private Integer enqCloseReasonId;

	private Integer enqCloseBrandId;

	private String enqCloseLostToModel;

	private String enqCloseRemarks;

	@JsonProperty(value = "cashLoan", required = true)
	@NotNull(message = "Cash/Loan Is Required")
	private String cashLoan;

	@JsonProperty(value = "dealPrice", required = true)
	private BigDecimal dealPrice;

	private Integer financierPartId;

	private Float estimatedFinanceAmnt;

	@JsonDeserialize(using = DateHandler.class)
	private Date financeLoggedInDate;

	private Integer financeTenureMonths;

	private Float financeAnuualInterestRate;

	private Float financeEmiAmnt;

	private String financeStatus;

	private Float disbursedFinanceAmnt;

	@JsonDeserialize(using = DateHandler.class)
	private Date disbursedDate;

	private BigDecimal finalExchangePrice;

	private Boolean subsidyRequired;

	private BigDecimal subsidyEstimatedAmount;

	private BigDecimal subsidyActualAmount;

	private BigDecimal marginAmount;

	private BigDecimal initialMarginAmountReceived;

	private BigDecimal remainingMarginAmountReceived;

	private BigDecimal totalReceived;

	private Integer countUpdate;

	private List<EnquiryEditExchangeDTLRequestModel> enquiryExchangeDTLList;
	private List<EnquiryEditCustSoilTypeRequestModel> enquiryCustSoilTypeList;
	private List<EnquiryEditCustFleetDTLRequestModel> enquiryCustFleetDTLList;
	private List<EnquiryEditCustCropRequestModel> enquiryCustCropList;
	private List<EnquiryEditAttachImagesRequestModel> enquiryAttachImgsList;
	private CustomerHDREditRequestModel customerHDRRequestModel;
}
