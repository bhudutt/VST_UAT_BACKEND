package com.hitech.dms.web.service.salesman.request;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.enquiry.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.enquiry.salesman.response.SalesmanListResponseModel;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Component
public class PostFeignClientFallback implements SalesmanServiceClient {

	@Override
	public SalesmanListResponseModel fetchSalesmanList(String authHeader, SalesmanListFormModel salesmanListFormModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse getSalesmanList(String authHeader, String dealerOrBranch, Long dealerOrBranchID) {
		// TODO Auto-generated method stub
		return null;
	}
}
