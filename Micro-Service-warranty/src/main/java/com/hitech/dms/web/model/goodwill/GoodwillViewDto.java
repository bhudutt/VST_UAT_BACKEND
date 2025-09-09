package com.hitech.dms.web.model.goodwill;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillPhoto;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;

import lombok.Data;

@Data
public class GoodwillViewDto {
	
	private String goodwillNo;
	
	private String goodwillDate;
	
	private String goodwillStatus;
	
	private String pcrNo;
	
	private Date pcrDate;
	
	private String pcrStatus;
	
	private BigInteger wcrId;
	
	private String wcrNo;
	
	private String wcrStatus;
	
	private String finalStatus;
	
	private String jobCardNo;
	
	private String jobCardDate;

	private String dateOfInstallation;

	private String customerName;

	private String model;

	private String address;

	private String MobileNo;

	private String dateOfFailure;
	
	private String natureOfFailure;
	
	private String failureType;

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
	
	private String complaintCode;
	
	private String complaintAggregate;
	
	private String serviceCategory;
	
	private String serviceType;
	
	private String repairOrderType;
	
	private String remark;
	
	private String rejectedReason;
	
	private String productType;//added on 07-08-2024
	
	List<CustomerVoiceDto> customerVoiceDto;
	
	List<ServiceHistoryDto> serviceHistoryDto;
	
	List<JobCardPcrPartDto> JobCardPcrPartDto;
	
    List<LabourChargeDTO> labourCharge;
    
    List<LabourChargeDTO> outSideLabourCharge;

    private List<WarrantyGoodwillPhoto> warrantyGoodwillPhoto;
	
}
