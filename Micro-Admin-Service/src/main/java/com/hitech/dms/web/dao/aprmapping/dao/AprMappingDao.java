package com.hitech.dms.web.dao.aprmapping.dao;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.admin.aprmapping.AprMappingRequestModel;
import com.hitech.dms.web.model.admin.aprmapping.AprMappingResponseModel;


public interface AprMappingDao {

	public AprMappingResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			AprMappingRequestModel stockUploadRequestModel,MultipartFile files);
}
