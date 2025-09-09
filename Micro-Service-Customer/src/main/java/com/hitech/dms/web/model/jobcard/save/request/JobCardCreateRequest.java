/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardCreateRequest {
	    private int branchId;
	    private int jobcardCatgId;
	    private int vinId;
	    private String chassisNumber;
	    private int serviceBookingId; 
	    private String engineNo;
	    private Integer sourceId;
	    private Integer placeOfServiceId;
	    private String manualJobCard;
	    private Date manualJobCardDate;
	    private Date jobCreationDate;
	    private String jobCardTime;
	    private String jobCardStatus;
	    private String currentHours;
	    private int previousHours;
	    private String meterNoChage;
	    private int totalHours;
	    private int serviceTypeId;
	    private int repairOrderTypeId;
	    private int serviceQuatationId;
	    private int invoiceId;
	    private int coustemberId;
	    private int inwardId;
	    private Double batteryVoltage;
	    private Integer installationDoneById;
	    private int representativeTypeId;
	    private String representativeName;
	    private Date estimateDate;
	    private String completationTime;
	    private String estimateAmount;
	    private String operatorManualNo;
	    private String operatorName;
	    private String operatorMobile;
	    private int applicationUsedById;
	    private String applicationUsedFor;
	    private int implementTypeId;
	    private String implementTypeOthers;
	    private int servicetechnicianId;
	    private int mechanic_one_id;
	    private int mechanic_two_id;
	    private int mechanic_three_id;
	}



