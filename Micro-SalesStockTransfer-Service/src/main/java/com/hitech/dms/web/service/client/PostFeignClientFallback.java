package com.hitech.dms.web.service.client;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;

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

}
