package com.hitech.dms.web.dao.partmaster.create;

import java.util.List;

import com.hitech.dms.web.model.partmaster.create.request.PartMasterFormRequestModel;
import com.hitech.dms.web.model.partmaster.create.request.PartSearchRequestModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterAutoListResponseModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterCreateResponseModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingResponseModel;

public interface PartMasterCreateDao {

	public PartMasterCreateResponseModel createPartMaster(String authorizationHeader, String userCode,
			PartMasterFormRequestModel requestModel);
	
	public List<PartMasterAutoListResponseModel> fetchPartNumber(String userCode,PartSearchRequestModel requestModel);
	
	public PartMasterFormRequestModel fetchPartNumberDetails(String userCode,String partNumber);
	
	public PartyMappingResponseModel fetchPartNumberDetailsnew(String userCode,String partNumber);

	
	
	

	
	
	
	
	
	
}
