package com.hitech.dms.web.dao.pcr;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.model.pcr.ComplaintAggregateDto;
import com.hitech.dms.web.model.pcr.CreatePcrDto;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.PCRApprovalRequestDto;
import com.hitech.dms.web.model.pcr.PcrApprovalResponseDto;
import com.hitech.dms.web.model.pcr.PcrCreateResponseDto;
import com.hitech.dms.web.model.pcr.PcrSearchRequestDto;
import com.hitech.dms.web.model.pcr.PcrSearchResponseDto;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;

public interface pcrSearchDao {
	ApiResponse<List<PcrSearchResponseDto>> pcrSearchList(String userCode, PcrSearchRequestDto requestModel);
	
	List<Map<String, Object>> fetchAllFailureType(String userCode, BigInteger roId);
	
	List<ComplaintAggregateDto> getComplaintAggregate(String authorizationHeader, String userCode);
	
	List<Map<String, Object>> getComplaintCode(Integer ID, String userCode);
	
	JobCardPcrViewDto fetchJobCardPcrView(String userCode, BigInteger roId, Integer flag);
	
	List<CustomerVoiceDto> fetchCustomerVoiceDto(String userCode, BigInteger roId, Integer flag);
	
	List<JobCardPcrPartDto> jobCardPcrPartDto(String userCode, BigInteger roId, Integer flag);
	
	List<LabourChargeDTO> fetchLabourCharges(String userCode, BigInteger roid, Integer flag);
	
	List<LabourChargeDTO> fetchOutsideLabourCharge(String userCode, BigInteger roid, Integer flag);
	
	List<ServiceHistoryDto> fetchServiceHistoryDto(String userCode, BigInteger roid, Integer flag);
	
	PcrCreateResponseDto createPCR(String authorizationHeader, String userCode,
			ServiceWarrantyPcr requestModel,Device device, List<MultipartFile> files);
	
	List<Map<String, Object>> autoSearchJcNo(String roNo);
	
	List<Map<String, Object>> autoSearchPcrNo(String pcrNo);
	
	PcrApprovalResponseDto approveRejectPCR(String userCode,PCRApprovalRequestDto requestModel);
	
	JobCardPcrViewDto viewPCR(String userCode, BigInteger pcrId);
	
	List<Map<String, Object>> getRejectedReason();
	
	List<Map<String, Object>> getProductType();
	
	List<Map<String, Object>> getDefectCode(Integer prodId, String userCode);
	
	List<Map<String, Object>> getDefectDesc(Integer defectId, String userCode);

}
