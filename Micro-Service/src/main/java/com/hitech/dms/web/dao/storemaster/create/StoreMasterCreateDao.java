package com.hitech.dms.web.dao.storemaster.create;

import java.util.List;

import com.hitech.dms.web.model.storemaster.create.request.StoreMasterFormRequestModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;
import com.hitech.dms.web.model.storemaster.create.response.StroeSearchResponseModel;

public interface StoreMasterCreateDao {

	public StoreMasterCreateResponseModel createStoreMaster(String authorizationHeader, String userCode,
			StoreMasterFormRequestModel requestModel);
	
	public List<StroeSearchResponseModel> searchStoreMaster(String authorizationHeader, String userCode,Integer page,Integer size,Integer dealerId);
	
	public StoreMasterCreateResponseModel updateStoreMaster(String userCode,Integer id,String isActive);
	
}
