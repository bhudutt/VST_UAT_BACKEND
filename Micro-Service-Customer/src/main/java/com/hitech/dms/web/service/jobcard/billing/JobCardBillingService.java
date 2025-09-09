package com.hitech.dms.web.service.jobcard.billing;

import java.util.List;


import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.jobcard.billing.request.JobBillingPLORequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingSearchRequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobCardBillingRequestModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCommonViewResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCreateResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCustomerTypeListResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingNumberSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingPLOResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingSearchResultResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingDetailsCommonResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSaleTypeResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardNumberSearchResponseModel;


public interface JobCardBillingService {

	List<JobCardBillingSaleTypeResponseModel> getSaleTypeList(String authorizationHeader, String userCode);

	List<JobCardBillingSearchResponseModel> fetchJobCardBillingList(String authorizationHeader, String userCode, String jobCardNo);

	JobCardBillingDetailsCommonResponseModel fetchJobCardBillingDetailsList(String authorizationHeader,
			String userCode, Integer roId, Integer flag);

	JobBillingCreateResponseModel create(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device);

	JobBillingSearchResultResponseModel fetchJobBillingSearchList(String authorizationHeader, String userCode,
			JobBillingSearchRequestModel requestModel);

	JobBillingCommonViewResponseModel fetchJobBillingViewList(String authorizationHeader, String userCode,
			Integer roBillingId,Integer flag);

	JobBillingCreateResponseModel jobCardBillingUpdate(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device);

	JobBillingPLOResponseModel fetchJobBillingPartLabourOutsideLBRList(String authorizationHeader, String userCode,
			JobBillingPLORequestModel requestModel);

	List<JobBillingNumberSearchResponseModel> fetchJobBillingNumberBySearchList(String authorizationHeader,
			String userCode, String billingNumber);

	List<JobCardNumberSearchResponseModel> fetchJobCardNumberBySearchList(String authorizationHeader,
			String userCode, String jobCardNumber);

	List<JobBillingCustomerTypeListResponseModel> fetchCustomerTypeList(String authorizationHeader, String userCode);

	
}
