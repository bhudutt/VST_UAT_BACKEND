package com.hitech.dms.web.dao.partreturn.search;

import java.util.List;

import com.hitech.dms.web.model.partreturn.request.PartReturnSearchRequestModel;
import com.hitech.dms.web.model.partreturn.response.PartReturnSearchListResultResponse;
import com.hitech.dms.web.model.partreturn.response.PartReturnViewResponseModel;

public interface PartReturnSearchDao {

	PartReturnSearchListResultResponse PartReturnSearchList(String userCode, PartReturnSearchRequestModel requestModel);

	List<PartReturnViewResponseModel> PartReturnViewList(String userCode, Integer returnId);

}
