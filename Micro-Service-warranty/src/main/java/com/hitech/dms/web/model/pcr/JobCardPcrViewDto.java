package com.hitech.dms.web.model.pcr;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.entity.pcr.WarrantyPcrPhotos;

import lombok.Data;

@Data
public class JobCardPcrViewDto {
	
	private String pcrNo;
	
	private Date pcrDate;
	
	private BigInteger wcrId;
	
	private String wcrNo;
	
	private String wcrStatus;
	
	private String finalStatus;
	
	private BigInteger goodwillId;
	
	private String goodwillNo;
	
	private String jobCardNo;
	
	private String jobCardDate;

	private Date dateOfInstallation;

	private String customerName;
	
	private String status;

	private String model;

	private String address;

	private String MobileNo;

	private String dateOfFailure;
	
	private String failureType;
	
//	private String natureOfFailure;
	private String productType; //added on 06-08-24

	private String registrationNumber;

	private String chassisNo;

	private String engineNo;
	
	private String vinNo;

	private String soldToDealer;

	private String serviceDealer;

	private String serviceDealerAddress;

	private Long MachineInventoryId;

	private String customerConcern;

	private String mechanicObservation;
	
	private BigInteger totalHour;

	private String dealerObservation;

	private String actionTaken;

	private String dealerRemarks;

	private String InstallationDate;

	private Long machineMasterId;
	
	private BigInteger complaintCodeId;
	
	private String complaintCode;
	
	private Integer complaintAggregateId;
	
	private String complaintAggregate;
	
	private String serviceCategory;
	
	private String serviceType;
	
	private String repairOrderType;
	
	private String remark;
	
	private String rejectedReason;
	
	List<CustomerVoiceDto> customerVoiceDto;
	
	List<ServiceHistoryDto> serviceHistoryDto;
	
	List<JobCardPcrPartDto> JobCardPcrPartDto;
	
    List<LabourChargeDTO> labourCharge;
    
    List<LabourChargeDTO> outSideLabourCharge;
    
    List<WarrantyPcrPhotos> warrantyPcrPhotos;

}
