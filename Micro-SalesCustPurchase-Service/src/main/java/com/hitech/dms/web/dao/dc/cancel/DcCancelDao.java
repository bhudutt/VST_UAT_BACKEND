package com.hitech.dms.web.dao.dc.cancel;

import com.hitech.dms.web.model.dc.cancel.request.DcCancelRequestModel;
import com.hitech.dms.web.model.dc.cancel.response.DcCancelResponseModel;

public interface DcCancelDao {
	public DcCancelResponseModel cancelDc(String authorizationHeader, String userCode,
			DcCancelRequestModel requestModel);
}
