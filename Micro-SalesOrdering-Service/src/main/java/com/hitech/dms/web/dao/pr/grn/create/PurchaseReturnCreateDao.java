package com.hitech.dms.web.dao.pr.grn.create;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;

import com.hitech.dms.web.model.pr.create.request.PurchaseReturnCreateRequestModel;
import com.hitech.dms.web.model.pr.create.response.PurchaseReturnCreateResponseModel;

public interface PurchaseReturnCreateDao {
	public PurchaseReturnCreateResponseModel createMachinePurchaseReturn(String authorizationHeader, String userCode,
			PurchaseReturnCreateRequestModel requestModel);
	
	public Resource loadFileAsResource(String fileName, String docPath, Long id,String path) throws MalformedURLException;
}
