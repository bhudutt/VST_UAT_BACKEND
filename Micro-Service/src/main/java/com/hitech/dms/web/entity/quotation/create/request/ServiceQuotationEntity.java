package com.hitech.dms.web.entity.quotation.create.request;

import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.hitech.dms.web.model.common.customer.CustomerModel;

import lombok.Data;

@Data
@Entity
@Table(name = "SV_QUOTATION_HDR")
public class ServiceQuotationEntity {

	@Id
	@Column(name = "Quotation_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer quotationId;
	
	@Column(name = "Source_ID")
	private Integer source;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "IsRoughEstimate")
	@Type(type = "yes_no")
	private Boolean isRoughEstimate;
	
	@Column(name = "RemarksCustomer")
	private String remarkscustomer;
	
	@Column(name = "Branch_Id")
	private Integer branchId;
	
	@Column(name = "KMReading")
	private String kilometerReading;
	
	@Column(name = "QuotationNumber", updatable = false)
	private String quotationNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Quotation_Date", updatable = false)
	private java.util.Date quotationDate;
	
	@Column(name = "Service_Category")
	private Integer serviceCategory;
	
	@Column(name = "Customer_Id")
	private Integer customerId;
	
	@Column(name = "Appointment_Id")
	private Integer appointmentId;
	
	@Column(name = "QuotationType")
	private String quotationTypeId;
	
	@Column(name = "Repair_Order_Id")
	private Integer repairOrderType;
	
	@Column(name = "QuotationRevSeq")
	private Integer quotationRevSeq;
	
	@Column(name = "Service_Type_Id")
	private Integer serviceTypeId;
	
	@Column(name = "IsUsed")
	@Type(type = "yes_no")
	private boolean used;

	@Column(name = "Ro_Id")
	private Integer roId;

	@Column(name = "Vin_Id")
	private Integer vinId;

	@Column(name = "SaleType")
	private String saleType;
	
	@Column(name = "IsCustomerApproved")
	@Type(type = "yes_no")
	private boolean customerApprovedStatus;

	@Column(name = "AmountApprovedCustomer")
	private BigDecimal amountApprovedCustomer;

	@Column(name = "ApprovalOnCustomer")
	private String approvalOnCustomer;

	@Column(name = "PaymentRefNo")
	private String paymentRefNo;

	@Column(name = "AmountReceived")
	private BigDecimal amountReceived;

	@Column(name = "IsApprovedSurveyor")
	@Type(type = "yes_no")
	private Boolean approvedSurveyorStatus;

	@Column(name = "IsEditable")
	@Type(type = "yes_no")
	private Boolean isEditable;
	
	@Column(name = "ApprovalOnSurveyor")
	private String approvalOnSurveyor;

	@Column(name = "AmountApprovedSurveyor")
	private BigDecimal amountApprovedSurveyor;

	@Column(name = "RemarksSurveyor")
	private String remarksSurveyor;
	
	@Column(name = "GRemarks")
	private String GRemarks;



	@Column(name = "BasicAmtPart")
	private BigDecimal basicAmountOnPart;

	@Column(name = "BasicAmtLabour")
	private BigDecimal basicAmountOnLabour;
	
	@Column(name = "BasicAmtOutsideLabour")
	private BigDecimal basicAmountOnOutsideLabour;

	@Column(name = "DiscountAmtPart")
	private BigDecimal discountAmountOnPart;

	@Column(name = "DiscountAmtLabour")
	private BigDecimal discountAmountOnLabour;
	
	@Column(name = "DiscountAmtOutsideLabour")
	private BigDecimal discountAmountOnOutsideLabour;

	@Column(name = "ChargeAmtPart")
	private BigDecimal chargesAmountOnPart;

	@Column(name = "ChargeAmtLabour")
	private BigDecimal chargesAmountOnLabour;
	
	@Column(name = "ChargeAmtOutsideLabour")
	private BigDecimal chargesAmountOnOutsideLabour;

	@Column(name = "TotalAmtPart")
	private BigDecimal totalAmountOnPart;

	@Column(name = "TotalAmtLabour")
	private BigDecimal totalAmountOnLabour;
	
	@Column(name = "TotalAmtOutsideLabour")
	private BigDecimal totalAmountOnOutsideLabour;

	@Column(name = "TotalQuotationAmount")
	private BigDecimal totalAmount;

	@Column(name = "PercentgDisPart")
	private BigDecimal percentgDisPart;

	@Column(name = "PercentgDisLabour")
	private BigDecimal percentgDisLabour;
	
	@Column(name = "PercentgDisOutsideLabour")
	private BigDecimal percentgDisOutsideLabour;

	@Column(name = "PercentgDisApprovedBy")
	private String percentgDisApprovedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AccidentDate")
	private java.util.Date accidentDate;

	@Column(name = "AccidentPlace")
	private String accidentPlace;

	@Column(name = "FIRNo")
	private String firNo;
	
	@Column(name = "CurrentHours")
	private String currentHours;
	
	@Column(name = "totalHours")
	private String totalHours;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIRDate")
	private java.util.Date firDate;

	@Column(name = "DriverName")
	private String driverName;

	@Column(name = "DLNumber")
	private String dLNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DLExpiryDate")
	private java.util.Date dLExpiryDate;

	@Column(name = "SurveyorName")
	private String surveyorName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SurveyorIntimatedOn")
	private java.util.Date surveyorIntimatedOn;

	@Column(name = "Vin_Ins_Id")
	private Integer vinInsId;
	
	@Column(name = "insurancePartyId")
	private Integer insurancePatyType;
	
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	@OneToMany(mappedBy = "serviceQuotationEntity")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<ServiceQuotationLabrEntity> serviceQuotationLabrEntity;
	
	@OneToMany(mappedBy = "serviceQuotationEntity")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<ServiceQuotationPartEntity> serviceQuotationPartEntity;
	
	@OneToMany(mappedBy = "serviceQuotationEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ServiceQuotationPartEntity> serviceQuotationPartList;
	
	@OneToMany(mappedBy = "serviceQuotationEntity")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<ServiceVerbatimEntity> serviceVerbatim;
	
	
	@OneToMany(mappedBy = "serviceQuotationEntity")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<ServiceQuotationOutsideLbabrEntity> serviceQuotationOutsideLabrEntity;
	
	private transient CustomerModel customerModel;
	
	
	private transient String registrationNumber;
	private transient Integer modelFamilyId;
	private transient Integer modelId;
	private transient String scode;
	private transient Integer orgQuotationIdCount;
	private transient String refNo;
	private transient String originalQuotationType;
	private transient String isApproved;
	private transient String custApprovedStatus;
	private transient Integer docNumberId;
	private transient String roStatus;
	private transient boolean check;
	
	
	public ServiceQuotationEntity initailize(ServiceQuotationEntity serviceQuotationEntity) {
		// super();
		this.branchId = serviceQuotationEntity.branchId;
		this.quotationNumber = serviceQuotationEntity.quotationNumber;
		this.quotationDate = serviceQuotationEntity.quotationDate;
		this.customerId = serviceQuotationEntity.customerId;
		this.insurancePatyType = serviceQuotationEntity.insurancePatyType;

		this.appointmentId = serviceQuotationEntity.appointmentId;
		this.quotationTypeId = serviceQuotationEntity.quotationTypeId;
		this.quotationRevSeq = serviceQuotationEntity.quotationRevSeq;
		this.serviceTypeId = serviceQuotationEntity.serviceTypeId;
		this.used = serviceQuotationEntity.used;
		this.roId = serviceQuotationEntity.roId;
		this.vinId = serviceQuotationEntity.vinId;
		this.saleType = serviceQuotationEntity.saleType;
		this.customerApprovedStatus = serviceQuotationEntity.customerApprovedStatus;
		this.amountApprovedCustomer = serviceQuotationEntity.amountApprovedCustomer;
		this.approvalOnCustomer = serviceQuotationEntity.approvalOnCustomer;
		this.paymentRefNo = serviceQuotationEntity.paymentRefNo;
		this.amountReceived = serviceQuotationEntity.amountReceived;
		this.approvalOnSurveyor = serviceQuotationEntity.approvalOnSurveyor;
		this.amountApprovedSurveyor = serviceQuotationEntity.amountApprovedSurveyor;
		this.remarksSurveyor = serviceQuotationEntity.remarksSurveyor;
		this.status = serviceQuotationEntity.status;
		System.out.println("status In entity we get is "+this.status);
		this.basicAmountOnPart = serviceQuotationEntity.basicAmountOnPart;
		this.basicAmountOnLabour = serviceQuotationEntity.basicAmountOnLabour;
		this.discountAmountOnPart = serviceQuotationEntity.discountAmountOnPart;
		this.discountAmountOnLabour = serviceQuotationEntity.discountAmountOnLabour;
		this.chargesAmountOnPart = serviceQuotationEntity.chargesAmountOnPart;
		this.chargesAmountOnLabour = serviceQuotationEntity.chargesAmountOnLabour;
		this.totalAmountOnPart = serviceQuotationEntity.totalAmountOnPart;
		this.totalAmountOnLabour = serviceQuotationEntity.totalAmountOnLabour;
		this.totalAmount = serviceQuotationEntity.totalAmount;
		this.percentgDisPart = serviceQuotationEntity.percentgDisPart;
		this.percentgDisLabour = serviceQuotationEntity.percentgDisLabour;
		this.percentgDisApprovedBy = serviceQuotationEntity.percentgDisApprovedBy;
		this.accidentDate = serviceQuotationEntity.accidentDate;
		this.accidentPlace = serviceQuotationEntity.accidentPlace;
		this.firNo = serviceQuotationEntity.firNo;
		this.firDate = serviceQuotationEntity.firDate;
		this.driverName = serviceQuotationEntity.driverName;
		this.dLNumber = serviceQuotationEntity.dLNumber;
		this.dLExpiryDate = serviceQuotationEntity.dLExpiryDate;
		this.surveyorName = serviceQuotationEntity.surveyorName;
		this.surveyorIntimatedOn = serviceQuotationEntity.surveyorIntimatedOn;
		this.vinInsId = serviceQuotationEntity.vinInsId;
		return this;
	}
	
}
