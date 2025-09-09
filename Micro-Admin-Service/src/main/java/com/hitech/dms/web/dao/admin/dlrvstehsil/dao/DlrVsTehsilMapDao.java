package com.hitech.dms.web.dao.admin.dlrvstehsil.dao;

import com.hitech.dms.web.model.admin.dlrvstehsil.request.DlrVsTehsilMapRequestModel;
import com.hitech.dms.web.model.admin.dlrvstehsil.response.DlrVsTehsilMapResponseModel;

public interface DlrVsTehsilMapDao {
	public DlrVsTehsilMapResponseModel mapDealerVsTehsil(String userCode, DlrVsTehsilMapRequestModel requestModel);
}
