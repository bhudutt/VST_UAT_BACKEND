package com.hitech.dms.web.dao.partybybranch.create;

import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.response.PartyCodeCreateResponseModel;

public interface PartyCodeCreateDao {

	public PartyCodeCreateResponseModel create(String authorizationHeader, String userCode,
			PartyCodeCreateRequestModel requestModel);
}
