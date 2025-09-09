package com.hitech.dms.web.dao.enquiry.search;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.entity.enquiry.ColumnEntity;
import com.hitech.dms.web.model.advancereport.ListPreference;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResultResponseModel;
import com.hitech.dms.web.model.enquiry.request.EnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportResponse;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportWithDealerWiseResponse;
import com.hitech.dms.web.model.enquiry.response.EnquriyReportByStateResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface EnquirySearchDao {
	public EnquiryListResultResponseModel fetchEnquiryList(String userCode,
			EnquiryListRequestModel enquiryListRequestModel);
	
	public List<EnquiryListResponseModel> fetchENQList(String userCode,
			EnquiryListRequestModel enquiryListRequestModel);

	/**
	 * @param userCode
	 * @param reportEnquiryListRequest
	 * @return
	 */
	public List<EnquiryReportResponse> getENQListForReport(String userCode, EnquiryReportRequest reportEnquiryListRequest);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);

	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatus
	 * @param outputStream
	 * @param reportName
	 * @throws JRException 
	 */
	public void printReport(net.sf.jasperreports.engine.JasperPrint jasperPrint, String format, String printStatus,
			OutputStream outputStream, String reportName) throws JRException;

	/**
	 * @param userCode
	 * @param reportEnquiryListRequest
	 * @return
	 */
	public List<EnquiryReportWithDealerWiseResponse> fetchENQWithStateAndDealerWiseForReport(String userCode,
			EnquiryReportRequest reportEnquiryListRequest);

	/**
	 * @param userCode
	 * @param reportEnquiryListRequest
	 * @return
	 */
	public List<EnquriyReportByStateResponse> fetchENQWithStateWiseReport(String userCode,
			EnquiryReportRequest reportEnquiryListRequest);

	/**
	 * @param userCode
	 * @param reportEnquiryListRequest
	 * @return
	 */
	public List<EnquiryReportWithDealerWiseResponse> fetchENQWithStateAndDealerWiseForSearch(String userCode,
			EnquiryReportRequest reportEnquiryListRequest);
	
	List<ListPreference> exportEnquiryListPreferences(String userCode);
	
	public EnquiryCreateResponseModel createColumnSetting(String userCode,List<ColumnEntity> requestPayload);
}
