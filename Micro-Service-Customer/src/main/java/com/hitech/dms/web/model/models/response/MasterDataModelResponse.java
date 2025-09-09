/**
 * 
 */
package com.hitech.dms.web.model.models.response;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */

@Data
public class MasterDataModelResponse {
	private Integer vinId;
	private String vinNumber;
	private Integer vinInsId;
	private String registrationNo;
	private String engineNo;
	private String chassisNo;
	private String customerName;
	private String customerCode;
	private String district;
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
	private Date SWExpiryDate;
	private Boolean pDIDoneStatus;
	private Boolean refurbishedStatus;
	private Date refurbishedDate;
	private Boolean govtVehicleStatus;
	private Boolean theftVehicleStatus;
	private Boolean totallyDamagedStatus;
	private Integer SWExpiryKm;
	private Integer SWExpiryHrs;
	private Date EWExpiryDate;
	private Date PolicyExpiryDate;
	private Integer EWExpiryKm;
	private Integer EWExpiryHrs;
	private String averageRun;
	private Date SWStartDate;
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
	private String vinCode;
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
	private Boolean isPDIdone;
	private Date warrantyEndDate;
	private Date extdWarrantyEndDate;
	private Date amcEndDate;
	private Integer warrantyKMUp;
	private Integer extdWarrantyKMUp;
	private Integer amcKMUp;
	private String omNumber;
	private String errMsg;
	private String totalHours;
	private String customerType;
	private String alternateMobNo;
	private String whatsappmobileno;
	private String country;
	private Date installationDate;
	private String profitcenter;
	private String series;
	private String segment;
	private String variant;
	private String itemNo;
	private String itemDesc;
	
	private String status;
	}
