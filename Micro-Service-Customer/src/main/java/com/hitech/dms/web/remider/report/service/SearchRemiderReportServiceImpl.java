package com.hitech.dms.web.remider.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.model.service.report.request.RemiderSearchBean;
import com.hitech.dms.web.model.service.report.request.ReminderReportReq;
import com.hitech.dms.web.model.service.report.request.ServicePlanStatusBean;
import com.hitech.dms.web.model.service.report.request.ServiceSearchReportBean;
import com.hitech.dms.web.model.service.report.response.ChessisDetails;
import com.hitech.dms.web.model.service.report.response.CustomerDtlResponse;
import com.hitech.dms.web.model.service.report.response.ModelResponse;
import com.hitech.dms.web.model.service.report.response.ServiceTypeResponse;
import com.hitech.dms.web.model.service.report.response.ZoneResponse;
import com.hitech.dms.web.remider.report.dao.SearchRemiderReportDao;

@Service
public class SearchRemiderReportServiceImpl implements SearchRemiderReportService {
	
	@Autowired
	private SearchRemiderReportDao searchRemiderReportDao;

	@Override
	public List<ServiceSearchReportBean> getsearchDetails(String authorizationHeader, String userCode, RemiderSearchBean bean) {
	
		return searchRemiderReportDao.getsearchDetails(userCode,bean);
	}

	@Override
	public List<ZoneResponse> getZoneList(String userCode) {
		
		return searchRemiderReportDao.getZoneList(userCode);
	}

	@Override
	public List<CustomerDtlResponse> fetchCustomerName(String userCode, ReminderReportReq requestModel) {
	
		return searchRemiderReportDao.fetchCustomerName(userCode, requestModel);
	}

	@Override
	public List<CustomerDtlResponse> getDealerList(String userCode, ReminderReportReq requestModel) {
		return searchRemiderReportDao.getDealerList(userCode, requestModel);
	}

	@Override
	public List<ChessisDetails> vinDetailsByChassisNo(String chassisNo, String userCode) {
		return searchRemiderReportDao.vinDetailsByChassisNo(chassisNo, userCode);
	}

	@Override
	public List<ModelResponse> getModelList(String userCode, Device device) {
	
		return searchRemiderReportDao.getModelList(userCode, device);
	}

	@Override
	public List<ServicePlanStatusBean> searchPlanStatus(String authorizationHeader, String userCode,
			RemiderSearchBean bean) {
		
		return searchRemiderReportDao.searchPlanStatus(userCode,bean);
	}

	@Override
	public List<ServiceTypeResponse> getServiceTypeList(String userCode, Device device) {
		
		return searchRemiderReportDao.getServiceTypeList(userCode);
	}

}
