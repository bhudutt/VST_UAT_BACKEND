package com.hitech.dms.web.service.admin.village.add;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.admin.village.request.VillageRequest;
import com.hitech.dms.web.model.admin.village.response.VillageResponse;
import com.hitech.dms.web.model.admin.village.response.VillageUploadExcelRes;

public interface VillageService {

	VillageResponse addVillage(String authorizationHeader, String userCode, @Valid VillageRequest requestModel,
			Device device);

	VillageUploadExcelRes addVillageUploadedFile(String authorizationHeader, String userCode, MultipartFile file);

}
