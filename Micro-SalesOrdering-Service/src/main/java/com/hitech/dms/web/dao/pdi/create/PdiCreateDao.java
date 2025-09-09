package com.hitech.dms.web.dao.pdi.create;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.pdi.OutwardPdiEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.model.pdi.create.request.PdiCreateRequestModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;

public interface PdiCreateDao {

	public PdiCreateResponseModel createPdi(String userCode,
			PdiEntity requestModel,List<MultipartFile> files);
	
	public PdiCreateResponseModel createOutwardPdi( String userCode,
			OutwardPdiEntity requestModel,List<MultipartFile> files);
	
	
}
