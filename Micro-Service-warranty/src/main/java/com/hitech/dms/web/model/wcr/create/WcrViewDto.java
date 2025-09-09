package com.hitech.dms.web.model.wcr.create;

import java.sql.Date;
import java.util.List;

import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;

import lombok.Data;

@Data
public class WcrViewDto {
	private String wcrNo;
	
	private String wcrDate;
	
	private String wcrType;
	
	private String pcrNo;
	
	private Date pcrDate;
	
	private Date pcrSubmittedDate;
	
	private String jobCardNo;
	
	private String jobCardDate;

	private String dateOfInstallation;

	private String customerName;

	private String model;

	private String address;

	private String MobileNo;

	private String dateOfFailure;

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
	
	private Double totalHour;

	private String dealerObservation;

	private String actionTaken;

	private String dealerRemarks;

	private String InstallationDate;

	private Long machineMasterId;
	
	List<CustomerVoiceDto> customerVoiceDto;
	
	List<ServiceHistoryDto> serviceHistoryDto;
	
	List<JobCardPcrPartDto> JobCardPcrPartDto;
	
    List<LabourChargeDTO> labourCharge;
    
    List<LabourChargeDTO> outSideLabourCharge;

//    private List<WarrantyGoodwillPhoto> warrantyGoodwillPhoto;

}
