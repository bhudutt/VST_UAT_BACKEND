package com.hitech.dms.web.service.jobcard.billing;

import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface JobCardBillingPrintService {

	JasperPrint PdfGeneratorReportForJobCardBill(HttpServletRequest request, String string, HashMap<String, Object> jasperParameter,
			String filePath);
	
	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream) throws JRException;

}
