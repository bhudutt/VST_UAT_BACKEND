package com.hitech.dms.web.service.report.invoiceSearch;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartCustResponse;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchList;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.ZoneResponse;

public interface InvoiceReportService {

	InvoiceReportSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device);

	List<partSearchResponseModel> fetchPartNumber(String userCode, InvoicePartNoRequest requestModel);

	List<KPDResponse> getKPDList(String userCode, InvoicePartNoRequest requestModel);

	List<InvoicePartCustResponse> fetchCustomerName(String userCode, InvoicePartNoRequest requestModel);

	List<ZoneResponse> getZoneList(String userCode);

}
