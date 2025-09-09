package com.hitech.dms.web.dao.spare.targetSetting;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.targetSetting.TargetSettingDtlEntity;
import com.hitech.dms.web.entity.targetSetting.TargetSettingHdrEntity;
import com.hitech.dms.web.model.spare.SparePartUploadResponseModel;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingDtlResponse;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingResponseModel;

public interface TargetSettingDao {

	HashMap<BigInteger, String> fetchTargetFor(String userType);

	TargetSettingResponseModel saveTargetSettingData(TargetSettingHdrEntity targetSettingHdrEntity,
			List<TargetSettingDtlEntity> targetSettingDtlEntityList, BigInteger targetHdrId, String userCode);

	List<SparePoCategoryResponse> getProductCategory(BigInteger partyCategoryId);

	TargetSettingDtlResponse checkIfPartyAlreadyExist(String targetFor, String productCategory, BigInteger partyId);

	List<TargetSettingResponseModel> fetchTargetSettingData(String userCode);

	TargetSettingResponseModel fetchTargetData(BigInteger id, String userCode, String targetFor,
			String productCategory);
	
	List<TargetSettingDtlResponse> fetchTargetDtlData(BigInteger id, String userCode, String isFor, String productCategory);
}
