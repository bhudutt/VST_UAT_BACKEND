package com.hitech.dms.web.dao.opex.template.upload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.opex.template.upload.request.OpexBudgetUploadRequestModel;
import com.hitech.dms.web.model.opex.template.upload.response.OpexBudgetUploadResponseModel;

public interface OpexBudgetUploadDao {
	public OpexBudgetUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			OpexBudgetUploadRequestModel requestModel, List<MultipartFile> files);
}
