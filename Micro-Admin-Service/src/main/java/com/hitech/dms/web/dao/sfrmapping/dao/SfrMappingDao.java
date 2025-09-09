package com.hitech.dms.web.dao.sfrmapping.dao;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.admin.sfrmapping.SfrMappingRequestModel;
import com.hitech.dms.web.model.admin.sfrmapping.SfrMappingResponseModel;

public interface SfrMappingDao {

	SfrMappingResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			SfrMappingRequestModel sfrUploadRequestModel, MultipartFile files);

}
