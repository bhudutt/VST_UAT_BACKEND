package com.hitech.dms.web.dao.allotment.enq.dtl;

import com.hitech.dms.web.model.allot.enq.dtl.request.MachineEnqDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.enq.dtl.response.MachineEnqDtlForAllotResponseModel;

public interface MachineEnqDtlForAllotDao {
	public MachineEnqDtlForAllotResponseModel fetchEnqDtlForAllot(String userCode,
			MachineEnqDtlForAllotRequestModel requestModel);
}
