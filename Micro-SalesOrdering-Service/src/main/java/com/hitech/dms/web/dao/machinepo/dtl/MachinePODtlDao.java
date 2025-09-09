package com.hitech.dms.web.dao.machinepo.dtl;

import com.hitech.dms.web.model.machinepo.dtl.request.MachinePODtlRequestModel;
import com.hitech.dms.web.model.machinepo.dtl.response.MachinePOHdrResponseModel;

public interface MachinePODtlDao {
	public MachinePOHdrResponseModel fetchMachinePODtl(String userCode, MachinePODtlRequestModel requestModel);
}
