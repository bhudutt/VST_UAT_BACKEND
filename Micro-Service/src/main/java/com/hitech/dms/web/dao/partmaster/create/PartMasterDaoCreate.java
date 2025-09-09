package com.hitech.dms.web.dao.partmaster.create;

import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingResponseModel;

public interface PartMasterDaoCreate {
	
	public PartyMappingResponseModel fetchPartNumberDetailsnew(String userCode,String partNumber,String isFor);

}
