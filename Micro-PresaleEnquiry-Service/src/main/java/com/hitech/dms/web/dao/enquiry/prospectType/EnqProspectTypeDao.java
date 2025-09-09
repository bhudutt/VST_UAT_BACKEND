package com.hitech.dms.web.dao.enquiry.prospectType;

import java.util.List;

import com.hitech.dms.web.model.enquiry.prospect.request.EnqProspectRequestModel;
import com.hitech.dms.web.model.enquiry.prospect.response.EnqProspectResponseModel;

public interface EnqProspectTypeDao {
	public List<EnqProspectResponseModel> fetchEnqProspectTypeList(String userCode, String enqOrFollowupdate,
			String edd);

	public List<EnqProspectResponseModel> fetchEnqProspectTypeList(String userCode,
			EnqProspectRequestModel enqProspectRequestModel);
}
