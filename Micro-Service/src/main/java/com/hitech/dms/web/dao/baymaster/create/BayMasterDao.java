package com.hitech.dms.web.dao.baymaster.create;

import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.baymaster.responselist.BayMasterResponseModel;
import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;

import java.util.List;

import com.hitech.dms.web.model.baymaster.create.request.BayMasterRequest;

public interface BayMasterDao {
	
	public BayMasterResponse createBayMaster(String userCode, List<BayMasterRequest> bayMasterRequest);

	public List<BayMasterResponseModel> fetchBayMasterList(String userCode, Integer dealerId, Integer page, Integer size);

	public List<BayTypeModel> fetchBayTypeList(String bayType);

	public BayMasterResponse updateBayMaster(String userCode, Integer bayMasterId, String isActive);

}
