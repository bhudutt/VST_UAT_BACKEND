package com.hitech.dms.web.dao.servicequotation.create;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.model.partmaster.create.response.ServiceQutationCreateResponseModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationCloseResponse;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface ServiceQuatationCreateDao {

	public ServiceQutationCreateResponseModel createServiceQuotation(String authorizationHeader, String userCode,
			ServiceQuotationModel requestModel);
	public ServiceQuotationModel fetchQuotationView(String userCode, BigInteger id);
	public List<ServiceQuotationModel> fetchQuotationNoList(String userCode, ServiceQuotationModel requestModel);
	public ServiceQuotationCloseResponse quotationCloseStatusUpdate(String userCode,BigInteger quotationId,String remarks,String status);
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath);
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException;

}
