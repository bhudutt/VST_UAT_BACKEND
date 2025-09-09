package com.hitech.dms.web.dao.registrationcertificate.create;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.registrationcertificate.create.response.RegistrationCreateResponseModel;

public interface RegistrationCertificateDao {
	
	public RegistrationCreateResponseModel updateRegistration(String userCode,SalesMachineVinMstEntity requestModel,List<MultipartFile> files);
}
