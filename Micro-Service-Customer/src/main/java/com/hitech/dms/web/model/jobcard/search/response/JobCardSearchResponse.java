/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardSearchResponse {
	private String action;
	private String jobcardNo;
	private Date jobCreationDate;
	private String jobCardStatus;
	private String branchName;
	private String chassisNo;
	private String vinNumber;
	private String jobCardCategory;
	private String registrationNo;
	private String engineNo;
	private String source;
	private String cutsomerName;
	private String mobileNo;
	private String serviceType;
	private String repairOrderType;
	private String serviceQuotation;
	private String serviceBooking;
	private String invoice;
	private BigInteger currentHours;
	private BigInteger previousHours;
	private BigInteger jobCardId;
    private Integer totalRecords;
	
	
//	private String placeOfService;
//	private String manualJobCard;
//	private Date manualJobCardDate;
//	private String jobCardNo;

//	private String jobCardTime;

	
//	private BigInteger meterHourChange;
//	private BigInteger totalHours;
	
//	private Date quotationDate;

//	private Date invoiceDate;
//	private String modelDesc;
//	private Integer inwardId;
//	private String profitCenter;
//	private Integer batteryVoltage;
//	private Date lrDate;
//	private String transportName;
//	private String lrNumber;
//	private String pdiDoneBy;
//	private String pendingInwardPdi;
//	private String completeInwardPdi;
//	private String starterMotorMakeNumber;
//	private String alternatorMakeNumber;
//	private String fIPMakeNumber;
//	private String batteryMakeNumber;
//	private String operatorMobileNumber;

//	private Integer categoryId;
//	private String installationDoneBy;
//	private String representativeType;
//	private String representativeName;
//	private Date estimateDate;
//	private String completionTime;
//	private BigDecimal estimateAmount;
//	private String operatorName;
//	private String applicationUsedBy;
	//private String applicationUsedFor;
	//private String implementType;
	//private String implementTypeOthers;
	//private String servicetechnician;
	//private String mechanic_one;
	//private String mechanic_two;
	//private String mechanic_three;


}
