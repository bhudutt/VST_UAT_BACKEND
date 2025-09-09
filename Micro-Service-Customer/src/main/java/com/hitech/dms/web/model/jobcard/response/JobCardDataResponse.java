/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardDataResponse {
	
	private String closingRemark;
	
	private String action;
	private String jobcardNo;
	private String branchName;
	private String chassisNo;
	private String vinNumber;
	private String registrationNo;
	private String serviceBooking;
	private Date serviceBookingDate;
	private String source;
	private String engineNo;
	private String placeOfService;
	private String manualJobCard;
	private Date manualJobCardDate;
	private String jobCardNo;
	private Date jobCreationDate;
	private String jobCardTime;
	private String jobCardStatus;
	private BigInteger currentHours;
	private BigInteger previousHours;
	private BigInteger meterNoChage;
	private BigInteger totalHours;
	private String serviceType;
	private String repairOrderType;
	private String serviceQuatation;
	private Date quationDate;
	private String invoice;
	private Date invoiceDate;
	private String modelDesc;
	private Integer inwardId;
	private String profitCenter;
	private String coustember;
	private BigDecimal batteryVoltage;
	private Date lrDate;
	private String transportName;
	private String lrNumber;
	private String pdiDoneBy;
	private String pendingInwardPdi;
	private String completeInwordPdi;
	private String starterMotorMakeNumber;
	private String alternatorMakeNumber;
	private String fIPMakeNumber;
	private String batteryMakeNumber;
	private String operatorMobileNumber;
	private String operatorManualNo;
	private String jobCardCategory;
	private Integer categoryId;
	private String installationDoneBy;
	private String representativeType;
	private String  representativeName;
	private Date estimateDate;
	private String completationTime;
	private BigDecimal estimateAmount;
	private String operatorName;
	private String applicationUsedBy;
	private String applicationUsedFor;
	private String implementType;
	private String implementTypeOthers;
	private String servicetechnician;
	private String mechanic_one;
	private String mechanic_two;
	private String mechanic_three;
	private BigInteger jobCardId;
	private String ROCancelReason;
	private Integer branchId;
	private BigInteger customerId;
	private String wcrStatus;
	
}
