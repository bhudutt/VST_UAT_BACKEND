package com.hitech.dms.web.dao.aop.template.upload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.aop.template.upload.request.AopTargetUploadRequestModel;
import com.hitech.dms.web.model.aop.template.upload.response.AopTargetUploadResponseModel;

public interface AopTargetUploadDao {
	public AopTargetUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			AopTargetUploadRequestModel requestModel, List<MultipartFile> files);
}
