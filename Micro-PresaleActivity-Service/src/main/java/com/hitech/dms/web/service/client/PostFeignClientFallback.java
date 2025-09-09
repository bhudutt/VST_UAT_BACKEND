package com.hitech.dms.web.service.client;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;

@Component
public class PostFeignClientFallback implements CommonServiceClient {

	@Override
	public HeaderResponse fetchActivitySourceListByPcId(String authHeader,
			ActivitySourceListRequestModel requestFormModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchActivitySourceListByPcId(String authHeader, Integer pcId, String isFor,
			String isIncludeInActive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse branchesUnderUserList(String authorizationHeader, String isInactiveInclude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse dealersUnderUserList(String authorizationHeader, String isInactiveInclude, String isFor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchModelsForSeriesSegment(String authorizationHeader,
			ModelsForSeriesSegmentRequestModel requestFormModel) {
		// TODO Auto-generated method stub
		return null;
	}
}
