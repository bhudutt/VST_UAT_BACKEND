package com.hitech.dms.web.service.report.stockSearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.report.stockSearch.StockReportSearchDao;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.PartStockSearchList;

@Service
public class StockReportServiceImpl implements StockReportService{
	
	
	@Autowired
	private StockReportSearchDao stockReportSearchDao;

	@Override
	public PartStockSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device) {
		
		return stockReportSearchDao.search(userCode, resquest, device);
	}

	@Override
	public List<KPDResponse> getPartStockKPDList(String userCode, InvoicePartNoRequest requestModel) {
		return stockReportSearchDao.getPartStockKPDList(userCode,requestModel);
	}

}
