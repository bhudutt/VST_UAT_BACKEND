package com.hitech.dms.web.service.report.invoiceSearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.report.invoiceSearch.InvoiceReportSearchDao;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartCustResponse;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchList;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.ZoneResponse;

@Service
public class InvoiceReportServiceImpl implements InvoiceReportService{
	
	@Autowired
	private InvoiceReportSearchDao  invoiceReportSearchDao;

	@Override
	public InvoiceReportSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device) {
	
		return invoiceReportSearchDao.search(userCode,resquest,device);
	}

	@Override
	public List<partSearchResponseModel> fetchPartNumber(String userCode, InvoicePartNoRequest requestModel) {
		
		return invoiceReportSearchDao.fetchPartNumber(userCode, requestModel);
	}

	@Override
	public List<KPDResponse> getKPDList(String userCode, InvoicePartNoRequest requestModel) {
		
		return invoiceReportSearchDao.getKPDList(userCode,requestModel);
	}

	@Override
	public List<InvoicePartCustResponse> fetchCustomerName(String userCode, InvoicePartNoRequest requestModel) {
	
		return invoiceReportSearchDao.fetchCustomerName(userCode,requestModel);
	}

	@Override
	public List<ZoneResponse> getZoneList(String userCode) {
	
		return invoiceReportSearchDao.getZoneList(userCode);
	}

}
