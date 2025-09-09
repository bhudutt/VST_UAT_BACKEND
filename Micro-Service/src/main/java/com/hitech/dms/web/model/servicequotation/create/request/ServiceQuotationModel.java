package com.hitech.dms.web.model.servicequotation.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.hitech.dms.web.model.common.customer.CustomerModel;

import lombok.Data;

@Data
public class ServiceQuotationModel {

	private Integer quotationId;
	private Integer source;
	private String originalQuotationType;
	private String action;
	private Integer icreEmpId;
	private Integer branchId;
	private Integer quotationRevisionNO;
	private String saleType;
	private String quotationNumber;
	private Integer modelFamilyId;
	private BigInteger modelId;
	private CustomerModel customerModel;
	private Boolean isRoughEstimate;
	private String remarkscustomer;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date quotationDate;
	private String quotationLabourIds;
	private String quotationPartIds;
	private String engineNumber;
	private Integer serviceCategory;
	private String roStatus;
	private String scode;
	private String supplementNumber;
	private Boolean isEditable;
	private boolean used;
	private Integer roId;
	private String registrationNumber;
	private Integer kilometerReading;
	private String currentHours;
	private String totalHours;
	private Integer customerId;
	private String appointmentNumber;
	private Integer appointmentId;
	private Integer vinId;
	private Integer vinInsId;
	private String remarks;
	private String forWhat;
	private String vinCode;
	private Date mfgInvoiceDate;
	private Date saleDate;
	private Date RetailDate;
	private BigDecimal basicAmountOnPart = BigDecimal.ZERO;
	private BigDecimal basicAmountOnLabour = BigDecimal.ZERO;
	private BigDecimal basicAmountOnOutsideLabour = BigDecimal.ZERO;
	private BigDecimal basicAmountTotal = BigDecimal.ZERO;
	private BigDecimal discountAmountOnPart = BigDecimal.ZERO;
	private BigDecimal discountAmountOnLabour = BigDecimal.ZERO;
	private BigDecimal discountAmountOnOutsideLabour = BigDecimal.ZERO;
	private BigDecimal discountAmountTotal = BigDecimal.ZERO;
	private BigDecimal chargesAmountOnPart = BigDecimal.ZERO;
	private BigDecimal chargesAmountOnLabour = BigDecimal.ZERO;
	private BigDecimal chargesAmountOnOutsideLabour = BigDecimal.ZERO;
	private BigDecimal chargesAmountTotal = BigDecimal.ZERO;
	private BigDecimal totalAmountOnPart = BigDecimal.ZERO;
	private BigDecimal totalAmountOnLabour = BigDecimal.ZERO;
	private BigDecimal totalAmountOnOutsideLabour = BigDecimal.ZERO;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private String quotationTypeId;
	private Integer quotationRevSeq;

	private List<ServiceVerbatimModel> verbatimList;
	private List<ServiceQuotationLabourModel> labourList;
	private List<ServiceQuotationPartModel> partList;
	private List<ServiceQuotationOutsideLabourModel> outsideLabourList;
	
	private Boolean approvedSurveyorStatus;
	private BigDecimal amountApprovedSurveyor = BigDecimal.ZERO;
	private String remarksSurveyor;
	private String approvalOnSurveyor;
	private boolean customerApprovedStatus;
	private double quotationAmount;
	private String approvalOnCustomer;
	private String paymentRefNo;
	private BigDecimal amountReceived = BigDecimal.ZERO;
	private BigDecimal amountApprovedCustomer = BigDecimal.ZERO;
	private String customerName;
	private String privilegeCustomer;
	private boolean vipDealer;
	private BigDecimal percentgDisLabour;
	private BigDecimal percentgDisOutsideLabour;
	private BigDecimal percentgDisPart;
	private String authorisedBy;
	private String insuranceCompany;
	private String driverName;
	private transient String isApproved;
	private transient String custApprovedStatus;
	private transient String approvalStatus;
	private String address1;
	private String dLNumber;
	private String address2;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dLExpiryDate;
	private String address3;
	private String surveyorName;
	private String pinCode;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date surveyorIntimatedOn;
	private String locality;
	private String accidentPlace;
	private String tehsil;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date accidentDate;
	private String city;
	private String state;
	private String country;
	private String firNo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date firDate;
	private String telephone;
	private String fax;
	private String EMail;
	private String policyNo;
	private String expiryDate;
	private String GRemarks;
	private Integer serviceTypeId;
	private String serviceTypeDesc;
	private transient Integer docNumberId;
	private String serverDate;
	private String serverDateTime;
	private String village;
	private String district;
	/* private List<ServiceRoMenuModel> serviceQuotationMenuList; */

	private String chassisNo;
	private String currentReading;
	private Integer repairOrderType;
	private Integer insurancePatyType;
	private String labourDiscout;
	private String mobNo;
	private String partDiscount;
	private String refNo;
	// filters
	public Integer page;
	public Integer size;
	private String quotationNo;
	private Integer pcId;
	private Integer orgHierID;
	private Integer dealerId;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private String fromDate;
	private String toDate;
	private String includeInActive;
	private String status;
	


	
}
