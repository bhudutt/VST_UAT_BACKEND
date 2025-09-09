package com.hitech.dms.web.dao.dealer.user.dtl;

import java.util.Map;

import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpAutoSearchRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpUserDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpDtlResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpUserDtlResponseModel;

public interface DealerUserDtlDao {
	public DealerEmpDtlResponseModel fetchEmployeeDTLForUserById(String userCode,
			DealerEmpDtlRequestModel requestModel);

	public Map<String, Object> fetchEmployeeAutoList(String userCode, DealerEmpAutoSearchRequestModel requestModel);
	
	public DealerEmpUserDtlResponseModel fetchEmpUserDetail(String userCode,
			DealerEmpUserDtlRequestModel requestModel);
}
