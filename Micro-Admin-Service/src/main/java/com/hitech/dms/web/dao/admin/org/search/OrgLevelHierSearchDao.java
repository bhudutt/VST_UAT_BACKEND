package com.hitech.dms.web.dao.admin.org.search;

import com.hitech.dms.web.model.admin.org.search.request.OrgLevelHierSearchRequestModel;
import com.hitech.dms.web.model.admin.org.search.response.OrgLevelHierSearchMainResponseModel;

public interface OrgLevelHierSearchDao {
	public OrgLevelHierSearchMainResponseModel fetchOrgHierSearchList(String userCode,
			OrgLevelHierSearchRequestModel requestModel);
}
