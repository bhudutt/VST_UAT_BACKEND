/**
 * 
 */
package com.hitech.dms.web.entity.enquiry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ENQ_HDR")
@Entity
@Data
public class EnquiryHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5042440987105847644L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enquiry_id")
	private BigInteger enquiryHdrId;

	@Column(name = "Branch_Id")
	private BigInteger branchId;

	@Column(name = "pc_id")
	private BigInteger pcId;

	@Column(name = "enquiry_number")
	private String enquiryNo;

	@Column(name = "enquiry_date", updatable = false)
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date enquiryDate;

	@Column(name = "Latitude")
	private transient String latitude;
	@Column(name = "Longitude")
	private transient String longitude;

	@Column(name = "enquiry_type_id")
	private BigInteger enquiryTypeid;

	@Column(name = "enquiry_stage_id")
	private Integer enquiryStageId;

//	@Type(type = "yes_no")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "app_enquiry_flag", columnDefinition = "TINYINT(1)")
	private Boolean appEnquiryFlag;

	@Column(name = "enquiry_status")
	private String enquiryStatus;

	@Column(name = "Enq_Source_Id")
	private Integer enquirySource;

	@Column(name = "Enq_remarks")
	private String enqRemark;

	@Column(name = "Digital_Source_ID")
	private Integer digitalSourceId;

	@Column(name = "Digital_Enq_Date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date digitalEnqDate;

	@Type(type = "yes_no")
	@Column(name = "Digital_Validation_Status")
	private Boolean digitalValidationStatus;

	@Column(name = "Digital_Validation_By")
	private String digitalValidationBy;

	@Column(name = "Activity_Source_ID")
	private Integer activitySourceID;

	@Column(name = "Activity_Date")
	private Date activityDate;

	@Column(name = "Activity_Plan_HDR_ID")
	private BigInteger activityPlanHDRId;

	@Column(name = "Salesman_Id")
	private BigInteger salesmanId;

	@Column(name = "Customer_Id", updatable = false)
	private BigInteger customerId;

	@Column(name = "LAND_IN_ACRES")
	private Float landInAcres;

	@Column(name = "Validation_By")
	private String validationBy;

	@Column(name = "Validation_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date validationDate;

	@Column(name = "Validation_Remarks")
	private String validationRemarks;

	@Column(name = "expected_purchase_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date expectedPurchaseDate;

	@Column(name = "next_follow_up_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date nextFollowupDate;

	@Column(name = "next_followup_activity_id")
	private BigInteger nextFollowupActivityId;

	@Column(name = "model_id")
	private BigInteger modelId;

	@Column(name = "variant")
	private String variant;

	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "purpose_of_purchase")
	private String purpose_of_purchase;

	@Column(name = "exchange_required")
	@Type(type = "yes_no")
	private Boolean isExchangeRequired;

	@Column(name = "exchange_received")
	@Type(type = "yes_no")
	private Boolean isExchangeReceived;

	@Column(name = "estimated_exchange_price")
	private Float estimateExchangePrice;

	@Column(name = "enq_close_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date enqCloseDate;

	@Column(name = "enq_close_reason_id")
	private Integer enqCloseReasonId;

	@Column(name = "enq_close_brand_id")
	private Integer enqCloseBrandId;

	@Column(name = "enq_close_lost_to_model")
	private String enqCloseLostToModel;

	@Column(name = "enq_close_remarks")
	private String enqCloseRemarks;

	@Column(name = "cash_loan")
	private String cashLoan;

	@Column(name = "deal_price")
	private BigDecimal dealPrice;

	@Column(name = "financier_party_id")
	private Integer financierPartId;

	@Column(name = "estimated_finance_amount")
	private Float estimatedFinanceAmnt;

	@Column(name = "finance_logged_in_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date financeLoggedInDate;

	@Column(name = "finance_tenure_months")
	private Integer financeTenureMonths;

	@Column(name = "finance_annual_interest_rate")
	private Float financeAnuualInterestRate;

	@Column(name = "finance_emi_amount")
	private Float financeEmiAmnt;

	@Column(name = "finance_status")
	private String financeStatus;

	@Column(name = "disbursed_finance_amount")
	private Float disbursedFinanceAmnt;

	@Column(name = "disbursed_date")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date disbursedDate;

	@Column(name = "final_exchange_price")
	private BigDecimal finalExchangePrice;

	@Column(name = "subsidy_required")
	@Type(type = "yes_no")
	private Boolean subsidyRequired;

	@Column(name = "subsidy_estimated_amount")
	private BigDecimal subsidyEstimatedAmount;

	@Column(name = "subsidy_actual_amount")
	private BigDecimal subsidyActualAmount;

	@Column(name = "margin_amount")
	private BigDecimal marginAmount;

	@Column(name = "initial_margin_amount_received")
	private BigDecimal initialMarginAmountReceived;

	@Column(name = "remaining_margin_amount_received")
	private BigDecimal remainingMarginAmountReceived;

	@Column(name = "total_received")
	private BigDecimal totalReceived;

	@Column(name = "count_update")
	private Integer countUpdate;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryFollowupEntity> enquiryFollowUpList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryTransferHistoryEntity> enquiryTransferHistoryList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryExchangeDTLEntity> enquiryExchangeDTLList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryCustSoilTypeEntity> enquiryCustSoilTypeList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryCustFleetDTLEntity> enquiryCustFleetDTLList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryCustCropEntity> enquiryCustCropList;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "enquiryHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<EnquiryAttachImagesEntity> enquiryAttachImgsList;
}
