package com.hitech.dms.web.remider.report.dao;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.service.report.request.RemiderSearchBean;
import com.hitech.dms.web.model.service.report.request.ReminderReportReq;
import com.hitech.dms.web.model.service.report.request.ServicePlanStatusBean;
import com.hitech.dms.web.model.service.report.request.ServiceSearchReportBean;
import com.hitech.dms.web.model.service.report.response.ChessisDetails;
import com.hitech.dms.web.model.service.report.response.CustomerDtlResponse;
import com.hitech.dms.web.model.service.report.response.ModelResponse;
import com.hitech.dms.web.model.service.report.response.ServiceTypeResponse;
import com.hitech.dms.web.model.service.report.response.ZoneResponse;

public interface SearchRemiderReportDao {

	List<ZoneResponse> getZoneList(String userCode);

	List<CustomerDtlResponse> fetchCustomerName(String userCode, ReminderReportReq requestModel);

	List<CustomerDtlResponse> getDealerList(String userCode, ReminderReportReq requestModel);

	List<ChessisDetails> vinDetailsByChassisNo(String chassisNo, String userCode);

	List<ServiceSearchReportBean> getsearchDetails(String userCode, RemiderSearchBean bean);

	List<ModelResponse> getModelList(String userCode, Device device);

	List<ServicePlanStatusBean> searchPlanStatus(String userCode, RemiderSearchBean bean);

	List<ServiceTypeResponse> getServiceTypeList(String userCode);



}
