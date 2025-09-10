package com.hitech.dms.web.dao.admin.org.add;

import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.admin.org.request.OrgLevelHierRequestModel;
import com.hitech.dms.web.model.admin.org.response.OrgLevelHierResponseModel;

public interface OrgLevelHierAddDao {

	public OrgLevelHierResponseModel addOrgLevelHier(String userCode, @Valid List<OrgLevelHierRequestModel> requestModel,
			Device device);
}
