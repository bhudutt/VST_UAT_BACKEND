package com.hitech.dms.web.dao.admin.autosearch;

import java.util.List;

import com.hitech.dms.web.model.admin.search.request.HoUserListRequestModel;
import com.hitech.dms.web.model.admin.search.response.HoUserListResponseModel;

public interface HoUserListDao {
	public List<HoUserListResponseModel> fetchAutoHoUserList(String userCode, HoUserListRequestModel requestModel);
}
