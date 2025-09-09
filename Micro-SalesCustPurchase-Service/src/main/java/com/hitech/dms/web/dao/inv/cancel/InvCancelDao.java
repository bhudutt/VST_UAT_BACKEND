package com.hitech.dms.web.dao.inv.cancel;

import com.hitech.dms.web.model.inv.cancel.request.InvCancelRequestModel;
import com.hitech.dms.web.model.inv.cancel.response.InvCancelResponseModel;

public interface InvCancelDao {
	public InvCancelResponseModel cancelInvoice(String authorizationHeader, String userCode,
			InvCancelRequestModel requestModel);
}
