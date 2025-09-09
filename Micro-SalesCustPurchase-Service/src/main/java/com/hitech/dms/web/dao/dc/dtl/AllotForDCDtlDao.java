package com.hitech.dms.web.dao.dc.dtl;

import com.hitech.dms.web.model.dc.dtl.request.AllotForDCDtlRequestModel;
import com.hitech.dms.web.model.dc.dtl.response.AllotForDCDtlResponseModel;

public interface AllotForDCDtlDao {
	public AllotForDCDtlResponseModel fetchAllotDtlForDc(String userCode, AllotForDCDtlRequestModel requestModel);
}
