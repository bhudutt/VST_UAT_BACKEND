package com.hitech.dms.web.dao.dc.create;

import com.hitech.dms.web.model.dc.create.request.DcCreateRequestModel;
import com.hitech.dms.web.model.dc.create.response.DcCreateResponseModel;

public interface DcCreateDao {
	public DcCreateResponseModel createDc(String authorizationHeader, String userCode,
			DcCreateRequestModel requestModel);
}
