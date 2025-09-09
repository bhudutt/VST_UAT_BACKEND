package com.hitech.dms.web.dao.inv.view;

import com.hitech.dms.web.model.inv.inv.view.request.InvViewRequestModel;
import com.hitech.dms.web.model.inv.inv.view.response.InvViewResponseModel;

public interface InvViewDao {
	public InvViewResponseModel fetchMachineInvDtl(String userCode, InvViewRequestModel requestModel);
}
