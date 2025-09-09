package com.hitech.dms.web.service.goodwill;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwill;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Transactional
public interface GoodWillService {
	
	ApiResponse<?> saveGoodwill(String authorizationHeader, String userCode,WarrantyGoodwill requestModel, 
			List<MultipartFile> files);
	
	ApiResponse<?> viewGoodwill(BigInteger goodwillId);
	
	ApiResponse<?>  autoSearchGoodwillNo(String goodwillNo);
	
	ApiResponse<?>  autoSearchJcNo(String roNo);
	
	ApiResponse<?>  autoSearchPcrNo(String pcrNo);
	
	ApiResponse<?>  autoSearchChassisNo(String chassisNo);
	
	ApiResponse<?>  goodwillSearchList(String userCode, GoodwillSearchRequestDto requestModel);
	
	ApiResponse<?>  approveRejectGoodwill(String userCode, GoodwillApprovalRequestDto requestModel);
		
}
