package com.hitech.dms.web.dao.stockSummary;

import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.model.stockSummary.DeliverySummaryRequest;
import com.hitech.dms.web.model.stockSummary.DeliverySummaryResponse;
import com.hitech.dms.web.model.stockSummary.StockSummaryResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface StockSummaryDao {

	public DeliverySummaryResponse deliverySummaryReportDao(DeliverySummaryRequest request,String userCode);

	public StockSummaryResponse stockSummaryReportDao(DeliverySummaryRequest request,String userCode);
	
	
	
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath);

	
	public JasperPrint ExcelGeneratorReportStockSummary(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath);

	
	
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException;
	
	public void printReportStockSummary(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException;
	
}
