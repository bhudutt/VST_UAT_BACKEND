package com.hitech.dms.web.dao.aop.search;

import com.hitech.dms.web.model.aop.search.request.AopTargetSearchRequestModel;
import com.hitech.dms.web.model.aop.search.response.AopTargetSearchMainResponseModel;

public interface AopTargetSearchDao {
	public AopTargetSearchMainResponseModel searchAopTargetList(String userCode,
			AopTargetSearchRequestModel requestModel);
}
