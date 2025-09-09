package com.hitech.dms.web.dao.grn.view;

import com.hitech.dms.web.model.grn.view.request.SalesGrnViewRequestModel;
import com.hitech.dms.web.model.grn.view.response.SalesGrnViewResponseModel;

public interface SalesGrnViewDao {
	public SalesGrnViewResponseModel fetchSalesGrnDetail(String userCode, SalesGrnViewRequestModel requestModel);
}
