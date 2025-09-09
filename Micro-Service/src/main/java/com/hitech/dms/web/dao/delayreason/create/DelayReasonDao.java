package com.hitech.dms.web.dao.delayreason.create;

import java.util.List;

import com.hitech.dms.web.model.delayreason.create.request.DelayReasonRequestName;
import com.hitech.dms.web.model.delayreason.create.response.DelayReasonResponseModel;

public interface DelayReasonDao {

	public DelayReasonResponseModel createDelayReasonMaster(String authorizationHeader,String userCode,DelayReasonRequestName requestModel);
	
	public List<DelayReasonResponseModel> searchDelayReason(String userCode);
}
