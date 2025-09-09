package com.hitech.dms.web.dao.grn.search.dao;

import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseMainModel;

public interface GrnSearchDao {
	public GrnSearchResponseMainModel salesGrnSearch(String userCode,
			GrnSearchRequestModel requestModel);
}
