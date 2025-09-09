package com.hitech.dms.web.dao.admin.org.edit;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.admin.org.edit.request.OrgLevelHierEditRequestModel;
import com.hitech.dms.web.model.admin.org.response.OrgLevelHierResponseModel;

public interface OrgLevelHierEditDao {
	public OrgLevelHierResponseModel editOrgLevelHier(String userCode, OrgLevelHierEditRequestModel requestModel,
			Device device);
}
