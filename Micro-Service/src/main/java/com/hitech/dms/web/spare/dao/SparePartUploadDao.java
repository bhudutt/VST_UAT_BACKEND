package com.hitech.dms.web.spare.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.spare.model.SparePartUploadResponseModel;
import com.hitech.dms.web.spare.model.SpareUploadRequestModel;

public interface SparePartUploadDao {

//	SparePartUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
//			SpareUploadRequestModel spareUploadRequestModel, List<MultipartFile> files);

	SparePartUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode, Integer branch,
			MultipartFile file);

}
