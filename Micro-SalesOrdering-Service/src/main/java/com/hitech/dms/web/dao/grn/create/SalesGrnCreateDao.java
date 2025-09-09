package com.hitech.dms.web.dao.grn.create;

import com.hitech.dms.web.model.grn.create.request.SalesGrnCreateRequestModel;
import com.hitech.dms.web.model.grn.create.response.SalesGrnCreateResponseModel;

public interface SalesGrnCreateDao {
	public SalesGrnCreateResponseModel createGrn(String authorizationHeader, String userCode,
			SalesGrnCreateRequestModel requestModel);
}
