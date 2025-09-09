package com.hitech.dms.web.dao.activity.dtl;

import com.hitech.dms.web.model.activity.dtl.request.ActualActivityDTLRequestModel;
import com.hitech.dms.web.model.activity.dtl.response.ActualActivityDTLResponseModel;

public interface ActualActivityDtlDao {
	public ActualActivityDTLResponseModel fetchActivityPlanHDRDTL(String userCode,
			ActualActivityDTLRequestModel requestModel);
}
