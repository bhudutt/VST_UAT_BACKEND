package com.hitech.dms.web.dao.dc.autolist;

import java.util.List;

import com.hitech.dms.web.model.dc.autolist.request.AllotForDCAutoListRequestModel;
import com.hitech.dms.web.model.dc.autolist.response.AllotForDCAutoListResponseModel;

public interface AllotForDCAutoListDao {
	public List<AllotForDCAutoListResponseModel> fetchAllotListForDc(String userCode,
			AllotForDCAutoListRequestModel requestModel);
}
