package com.hitech.dms.web.dao.wcr.create;

import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.wcr.WarrantyWcrEntity;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;
import com.hitech.dms.web.model.wcr.create.WcrCreateResponseDto;

public interface WcrCreateDao {
	
	JobCardPcrViewDto fetchJobCardPcrView(String userCode, BigInteger roId, Integer flag);
	
	List<CustomerVoiceDto> fetchCustomerVoiceDto(String userCode, BigInteger roId, Integer flag);
	
	List<JobCardPcrPartDto> jobCardPcrPartDto(String userCode, BigInteger roId, Integer flag);
	
	List<LabourChargeDTO> fetchLabourCharges(String userCode, BigInteger roid, Integer flag);
	
	List<LabourChargeDTO> fetchOutsideLabourCharge(String userCode, BigInteger roid, Integer flag);
	
	List<ServiceHistoryDto> fetchServiceHistoryDto(String userCode, BigInteger roid, Integer flag);
	
	WcrCreateResponseDto createWCR(String authorizationHeader, String userCode,
			WarrantyWcrEntity requestModel, Device device);

}
