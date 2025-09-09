package com.hitech.dms.web.dao.spare;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.spare.SparePartUploadResponseModel;

public interface SparePartUploadDao {

//	SparePartUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
//			SpareUploadRequestModel spareUploadRequestModel, List<MultipartFile> files);

	SparePartUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode, Integer branch,
			MultipartFile file);

}
