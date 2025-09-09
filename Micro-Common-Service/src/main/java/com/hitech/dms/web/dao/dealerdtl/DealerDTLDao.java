package com.hitech.dms.web.dao.dealerdtl;

import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;

public interface DealerDTLDao {
	public DealerDTLResponseModel fetchDealerDTLByDealerId(String userCode, DealerDTLRequestModel requestModel);
}
