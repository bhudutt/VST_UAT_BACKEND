package com.hitech.dms.web.service.client;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;

@Component
public class PostFeignClientFallback implements CommonServiceClient {

	@Override
	public HeaderResponse fetchDealerDTLByDealerId(String authorizationHeader, DealerDTLRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchMainBranchDtlByDealerId(String authorizationHeader, BranchDTLRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchBranchDtlByBranchId(String authorizationHeader, BranchDTLRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchModelListByPcId(String authorizationHeader, Integer pcId, String isFor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse fetchStateDtlByStateID(String authorizationHeader, BigInteger stateId) {
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
