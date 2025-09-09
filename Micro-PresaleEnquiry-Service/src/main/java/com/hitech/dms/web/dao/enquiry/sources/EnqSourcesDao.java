package com.hitech.dms.web.dao.enquiry.sources;

import java.util.List;

import com.hitech.dms.web.model.enquiry.sources.request.EnqSourcesRequestModel;
import com.hitech.dms.web.model.enquiry.sources.response.EnqSourcesResponseModel;

public interface EnqSourcesDao {
	public List<EnqSourcesResponseModel> fetchEnqSourcesList(String userCode, String isApplicableFor);

	public List<EnqSourcesResponseModel> fetchEnqSourcesList(String userCode,
			EnqSourcesRequestModel sourcesRequestModel);
}
