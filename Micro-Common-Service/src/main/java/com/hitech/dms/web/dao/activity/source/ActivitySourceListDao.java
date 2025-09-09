package com.hitech.dms.web.dao.activity.source;

import java.util.List;

import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;
import com.hitech.dms.web.model.activity.source.response.SourceListResponseModel;

public interface ActivitySourceListDao {
	public List<ActivitySourceListResponseModel> fetchActivitySourceListByPcId(String userCode, Integer pcId,
			String isFor, String isIncludeInActive);

	public List<ActivitySourceListResponseModel> fetchActivitySourceListByPcId(
			ActivitySourceListRequestModel activitySourceListRequestModel);

	public List<SourceListResponseModel> fetchSourceList(String userCode, Integer pcId, String isFor,
			String isIncludeInActive);

	public List<SourceListResponseModel> fetchSourceList(ActivitySourceListRequestModel activitySourceListRequestModel);
}
