package com.hitech.dms.web.service.wcr;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.wcr.WarrantyWcrEntity;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.wcr.create.WcrCreateResponseDto;
import com.hitech.dms.web.model.wcr.search.WCRApprovalRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrApprovalResponseDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchRequestDto;

/**
 * @author mahesh.kumar
 */
@Transactional
public interface wcrService {
	
	public JobCardPcrDto fetchDetailsForWcr(String userCode, BigInteger roId);
	
	WcrCreateResponseDto createWCR(String authorizationHeader, String userCode,
			WarrantyWcrEntity requestModel, Device device);
	
	List<Map<String, Object>> autoSearchWcrNo(String wcrNo);
	
	List<Map<String, Object>> autoSearchPcrNo(String pcrNo);
	
	public ApiResponse<?> wcrSearchList(String userCode, WcrSearchRequestDto requestModel);
	
	WcrApprovalResponseDto approveRejectWCR(String userCode, WCRApprovalRequestDto requestModel);
	
//	ApiResponse<?> viewWcr(BigInteger wcrId);

}
