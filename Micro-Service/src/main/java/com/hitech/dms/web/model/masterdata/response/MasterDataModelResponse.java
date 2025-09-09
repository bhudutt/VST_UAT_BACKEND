package com.hitech.dms.web.model.masterdata.response;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class MasterDataModelResponse {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private BigInteger vinId;
	private Integer vinInsId;
	private String registrationNo;
	private String engineNo;
	private String displayValue;
	private String chassisNo;
	private String customerName;
	private String quotationNumber;
	private String customerCode;
	private String distributionChannel;
	private String modelGroupCode;
	private String modelGroupDesc;
	private String modelVariantDesc;
	private String modelFamilyDesc;
	private String modelPlatform;
	private Integer seatCapacity;
	private String fscBooklet;
	private String odometerReading;
	private Date mfgInvoiceDate;
	private String oemPriviledgeCust;
	private String ssiCategory;
	private String csiCategory;
	private String qsiCategory;
	private String relationMgr;
	private String avgKMPerDay;
	private String vehicleId;
	private String soldBy;
	private String colorCode;
	private String modelDesc;
	private Date retailDate;
	private Date sWExpiryDate;
	private Boolean pDIDoneStatus;
	private Boolean refurbishedStatus;
	private Date refurbishedDate;
	private Boolean govtVehicleStatus;
	private Boolean theftVehicleStatus;
	private Boolean totallyDamagedStatus;
	private Integer SWExpiryKm;
	private Integer SWExpiryHrs;
	private Date eWExpiryDate;
	private Date PolicyExpiryDate;
	private Integer EWExpiryKm;
	private Integer EWExpiryHrs;
	private String averageRun;
	private Date sWStartDate;
	private Date mgfWarrantyStartDate;
	private Date extWarrantyStartDate;
	private Integer CustomerId;
	private String CustAddLine1;
	private String CustAddLine2;
	private String CustAddLine3;
	private String pinCode;
	private String localityName;
	private String tehsilDesc;
	private String stateDesc;
	private String cityDesc;
	private String mobileNumber;
	private String fax;
	private String email;
	private Date saleDate;
	private Date DeliveryNoteDate;
	private transient String userName;
	private transient String userMobileNumber;
	private String driverName;
	private String driverMobNo;
	private String driverDlNo;
	private Date driverDlExpiryDate;
	private File file;
	private String fileName;
	private byte[] fileContent;
	private String contentType;
	private String amcPolicyNo;
	private String amcRegNumber;
	private Date amcExpiryDate;
	private Integer vinUserID;
	private String nextServiceDueDate;
	private String nextDueService;
	private String vinNumber;
	private String modelCode;
	private String divisionDesc;
	private Date ewStartDate;
	private Date amcStartDate;
	private String registrationstatus;
	private String mfgInvoiceNumber;
	private Date fcExpiryDate;
	private Date fcIssueDate;
	private String fcNumber;
	private String applicationType;
	private String bodyType;
	private Integer amcExpiryKm;
	private String amcPlan;
	private BigDecimal partDiscountPercentage;
	private BigDecimal labourDiscountPercentage;
	private String authorizeBy;
	private Integer modelFamilyId;
	private String isPDIdone;
	private Date warrantyEndDate;
	private Date extdWarrantyEndDate;
	private Date amcEndDate;
	private Integer warrantyKMUp;
	private Integer extdWarrantyKMUp;
	private Integer amcKMUp;
	private String omNumber;
	private transient List<VehicleRegistrationDetailModel> vehicleRegistrationDetailList;
	private transient PartyAddressDetailsModel partyAddressDetailsEntity;
	private String errMsg;
	private String totalHours;
	private String customerType;
	private String alternateMobNo;
	private String whatsappmobileno;
	private String country;
	
	private String installationDate;
	private String profitcenter;
	private String series;
	private String segment;
	private String variant;
	private String itemNo;
	private String itemDesc;
	
	private String status;
	private Integer serviceId;
	private String serviceVal;
	private String companyName;
	private String district;
	
	
	
}
