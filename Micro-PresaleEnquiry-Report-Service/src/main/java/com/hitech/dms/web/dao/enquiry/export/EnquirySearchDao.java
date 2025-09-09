package com.hitech.dms.web.dao.enquiry.export;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.model.enquiry.export.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.export.EnquiryReport2Response;
import com.hitech.dms.web.model.enquiry.export.EnquiryResponse;
import com.hitech.dms.web.model.enquiry.export.SalesManModel;
import com.hitech.dms.web.model.enquiry.list.request.SalesEnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.list.response.SalesEnquiryReportResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface EnquirySearchDao {

	public EnquiryReport2Response fetchEnquiryList(String userCode,
			EnquiryListRequestModel enquiryListRequestModel);

	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath);

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException;
	
	public List<SalesManModel> fetchSalesManList(String userCode,String dealerId);
	
	public List<String> searchDocumentNo(String enquiryNo,String userCode);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	public List<SalesEnquiryReportResponse> fetchSalesEnquiryList(String userCode,
			SalesEnquiryReportRequest requestModel);
	
	

}