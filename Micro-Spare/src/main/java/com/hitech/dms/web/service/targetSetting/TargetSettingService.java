package com.hitech.dms.web.service.targetSetting;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.targetSetting.request.TargetSettingRequestModel;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingResponseModel;

public interface TargetSettingService {

//	TargetSettingResponseModel validateUploadedFile(String authorizationHeader, String userCode,
//			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file);

	TargetSettingResponseModel uploadExcel(String authorizationHeader, String userCode,
			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file);

	TargetSettingResponseModel submitTargetData(String authorizationHeader, String userCode,
			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file);

	HashMap<BigInteger, String> fetchTargetFor(String userType);

	List<SparePoCategoryResponse> getProductCategory(BigInteger partyCategoryId);

	List<TargetSettingResponseModel> fetchTargetSettingData(String userCode);

	TargetSettingResponseModel fetchTargetSettingHdrAndDtl(BigInteger targetHdrId, String userCode);

}
