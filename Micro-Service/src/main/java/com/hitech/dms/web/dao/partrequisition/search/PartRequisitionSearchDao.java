package com.hitech.dms.web.dao.partrequisition.search;

import java.util.List;

import com.hitech.dms.web.model.partrequisition.search.request.PartRequisitionSearchRequestModel;
import com.hitech.dms.web.model.partrequisition.search.response.PartRequisitionSearchListResultResponse;
import com.hitech.dms.web.model.partrequisition.search.response.SparePartRequisitionViewResponseModel;

public interface PartRequisitionSearchDao {
	
	public PartRequisitionSearchListResultResponse PartRequitionSearchList(String userCode,
			PartRequisitionSearchRequestModel requestModel);

	public List<SparePartRequisitionViewResponseModel> PartRequisitionViewList(String userCode, Integer requisitionId);

}
