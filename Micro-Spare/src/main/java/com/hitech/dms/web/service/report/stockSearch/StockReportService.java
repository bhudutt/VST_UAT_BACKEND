package com.hitech.dms.web.service.report.stockSearch;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.PartStockSearchList;

public interface StockReportService {

	PartStockSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device);

	List<KPDResponse> getPartStockKPDList(String userCode, InvoicePartNoRequest requestModel);

}
