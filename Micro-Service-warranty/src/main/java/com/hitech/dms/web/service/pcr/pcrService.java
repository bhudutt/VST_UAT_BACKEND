package com.hitech.dms.web.service.pcr;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.core.io.Resource;
import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.model.pcr.ComplaintAggregateDto;
import com.hitech.dms.web.model.pcr.CreatePcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.PCRApprovalRequestDto;
import com.hitech.dms.web.model.pcr.PcrApprovalResponseDto;
import com.hitech.dms.web.model.pcr.PcrCreateResponseDto;
import com.hitech.dms.web.model.pcr.PcrSearchRequestDto;
import com.hitech.dms.web.model.pcr.PcrSearchResponseDto;


/**
 * @author mahesh.kumar
 */
@Transactional
public interface pcrService {
	public ApiResponse<?> pcrSearchList(String userCode, PcrSearchRequestDto requestModel);
	
	public List<Map<String, Object>> fetchAllFailureType(String userCode, BigInteger roId);
	
	List<ComplaintAggregateDto> getComplaintAggregate(String authorizationHeader, String userCode);
	
	List<Map<String, Object>> getComplaintCode(Integer ID, String userCode);
	
	public JobCardPcrDto fetchJobCardForPcr(String userCode, BigInteger roId);
	
	public PcrCreateResponseDto createPCR(String authorizationHeader, String userCode,
			ServiceWarrantyPcr requestModel, List<MultipartFile> files, Device device);
	
	List<Map<String, Object>> autoSearchJcNo(String roNo);
	
	List<Map<String, Object>> autoSearchPcrNo(String pcrNo);
	
	PcrApprovalResponseDto approveRejectPCR(String userCode, PCRApprovalRequestDto requestModel);
	
	JobCardPcrViewDto viewPCR(String userCode, BigInteger pcrId);
	
	List<Map<String, Object>> getRejectedReason();
	
	List<Map<String, Object>> getProductType();
	
	List<Map<String, Object>> getDefectCode(Integer prodId, String userCode);
	
	List<Map<String, Object>> getDefectDesc(Integer defectId, String userCode);
	
	Resource getImages(String fileName, BigInteger id, String moduleName, String userCode);
	

}
