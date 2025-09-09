package com.hitech.dms.web.dao.admin.search;

import com.hitech.dms.web.model.admin.search.request.HoUserSearchRequestModel;
import com.hitech.dms.web.model.admin.search.response.HoUserSearchMainResponseModel;

public interface HoUserSearchDao {
	public HoUserSearchMainResponseModel fetchHoUserSearch(String userCode, HoUserSearchRequestModel requestModel);
}
