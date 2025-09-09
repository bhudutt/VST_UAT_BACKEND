package com.hitech.dms.web.dao.dc.view;

import com.hitech.dms.web.model.dc.view.request.DcViewRequestModel;
import com.hitech.dms.web.model.dc.view.response.DcViewResponseModel;

public interface DcViewDao {
	public DcViewResponseModel fetchMachineDcDtl(String userCode, DcViewRequestModel requestModel);
}
