package com.hitech.dms.web.service.jobcard.billing;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.jobcard.billing.JobCardBillingDao;
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


@Service
public class JobCardBillingServiceImpl implements JobCardBillingService{
	
    @Autowired
	private JobCardBillingDao jobCardBillingDao;
	@Override
	
	public List<JobCardBillingSaleTypeResponseModel> getSaleTypeList(String authorizationHeader, String userCode) {
		List<JobCardBillingSaleTypeResponseModel>responseModel=jobCardBillingDao.fetchSaleTypeList(authorizationHeader, userCode);
		return responseModel;
	}
	@Override
	public List<JobCardBillingSearchResponseModel> fetchJobCardBillingList(String authorizationHeader,
			String userCode, String jobCardNo) {
		List<JobCardBillingSearchResponseModel>responseModel=jobCardBillingDao.fetchJobCardBillingList(authorizationHeader, userCode, jobCardNo);
		return responseModel;
	}
	@Override
	public JobCardBillingDetailsCommonResponseModel fetchJobCardBillingDetailsList(String authorizationHeader,
			String userCode, Integer roId, Integer flag) {
		JobCardBillingDetailsCommonResponseModel commonResponseModel =jobCardBillingDao.fetchJobCardBillingDetailsList(authorizationHeader,userCode,roId, flag);
		return commonResponseModel;
	}
	@Override
	public JobBillingCreateResponseModel create(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device) {
		    JobBillingCreateResponseModel responseModel=jobCardBillingDao.create(authorizationHeader,userCode,requestModel,device);
		return responseModel;
	}
	@Override
	public JobBillingSearchResultResponseModel fetchJobBillingSearchList(String authorizationHeader, String userCode,
			JobBillingSearchRequestModel requestModel) {
		JobBillingSearchResultResponseModel responseModel= jobCardBillingDao.fetchJobBillingSearchList(authorizationHeader,userCode,requestModel);
		return responseModel;
	}
	@Override
	public JobBillingCommonViewResponseModel fetchJobBillingViewList(String authorizationHeader, String userCode,
			Integer roBillingId, Integer flag) {
		JobBillingCommonViewResponseModel responseModel=jobCardBillingDao.fetchJobBillingViewList(authorizationHeader,userCode,
				roBillingId, flag);
		return responseModel;
	}
	
	@Override
	public JobBillingCreateResponseModel jobCardBillingUpdate(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device) {
		return jobCardBillingDao.jobCardBillingUpdate(authorizationHeader,userCode,requestModel,device);
	}
	@Override
	public JobBillingPLOResponseModel fetchJobBillingPartLabourOutsideLBRList(String authorizationHeader,
			String userCode, JobBillingPLORequestModel requestModel) {
		
		return jobCardBillingDao.fetchJobBillingPartLabourOutsideLBRList(authorizationHeader,
				 userCode,  requestModel);
	}
	@Override
	public List<JobBillingNumberSearchResponseModel> fetchJobBillingNumberBySearchList(String authorizationHeader,
			String userCode, String billingNumber) {
		return jobCardBillingDao.fetchJobBillingNumberBySearchList(authorizationHeader,
				 userCode,  billingNumber);
	}
	@Override
	public List<JobCardNumberSearchResponseModel> fetchJobCardNumberBySearchList(String authorizationHeader,
			String userCode, String jobCardNumber) {
		
		return jobCardBillingDao.fetchJobCardNumberBySearchList(authorizationHeader,
				 userCode,  jobCardNumber);
	}
	@Override
	public List<JobBillingCustomerTypeListResponseModel> fetchCustomerTypeList(String authorizationHeader,
			String userCode) {
		
		return jobCardBillingDao.fetchCustomerTypeList(authorizationHeader,
				 userCode);
	}
	


}
