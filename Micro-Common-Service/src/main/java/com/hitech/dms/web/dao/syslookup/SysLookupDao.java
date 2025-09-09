package com.hitech.dms.web.dao.syslookup;

import java.util.List;

import com.hitech.dms.web.model.lookup.request.SysLookupRequestModel;
import com.hitech.dms.web.model.lookup.response.SysLookupResponseModel;

public interface SysLookupDao {
	public List<SysLookupResponseModel> fetchSysLookupList(String userCode, String lookupType, String includeInactive);

	public List<SysLookupResponseModel> fetchSysLookupList(String userCode,
			SysLookupRequestModel sysLookupRequestModel);
}
