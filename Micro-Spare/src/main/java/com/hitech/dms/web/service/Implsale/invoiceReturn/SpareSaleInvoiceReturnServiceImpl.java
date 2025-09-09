package com.hitech.dms.web.service.Implsale.invoiceReturn;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.dao.spare.sale.invoiceReturn.SpareSaleInvoiceReturnDao;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnApproveStatus;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetail;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetailSaveResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnRequestModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnSearchResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SearchInvoiceReturnRequest;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareInvoiceRetunSearchModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.UploadDisagreeDocumentModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.VstWithoutRefPartDtl;
import com.hitech.dms.web.service.sale.invoiceReturn.SpareSaleInvoiceReturnService;

import net.sf.jasperreports.engine.JasperPrint;

@Service
public class SpareSaleInvoiceReturnServiceImpl implements SpareSaleInvoiceReturnService {

	@Autowired
	SpareSaleInvoiceReturnDao dao;
	
	
	@Override
	public InvoiceReturnDetail getInvoiceDetailByDOcNo(String mrnNo, String userCode) {
		return dao.getInvoiceDetailByDOcNo(mrnNo, userCode);
	}


	@Override
	public InvoiceReturnDetail getInvoiceDetailByClaimGeneration(String mrnNo, String userCode) {
		return dao.getInvoiceDetailByClaimGenerationNo(mrnNo, userCode);
	}


	@Override
	public InvoiceReturnDetailSaveResponse saveInvoiceReturnDetail(InvoiceReturnRequestModel requestData,
			String userCode) {
		// TODO Auto-generated method stub
		return dao.saveInvoiceReturnDetail(requestData,userCode);
	}


	@Override
	public List<SpareInvoiceRetunSearchModel> searchInvoiceReturn(String searchType, String searchText, String userCode) {
		return dao.searchInvoiceReturnDao(searchType, searchText, userCode);
	}


	@Override
	public InvoiceReturnApproveStatus updateInvoiceReturnStatus(InvoiceReturnRequestModel request,String userCode) {
		return dao.updateInvoiceReturnStatus(request,userCode);
	}


	@Override
	public InvoiceReturnSearchResponse searchInvoiceReturnByDate(SearchInvoiceReturnRequest request,
			String userCode) {
		return dao.searchInvoiceReturnByDateDao(request,  userCode);
	}

	
	


	@Override
	public UploadDisagreeDocumentModel uploadDisagreeDocument(UploadDisagreeDocumentModel request, String userCode) {
		return dao.uploadDisagreeDocument(request ,userCode);
	}


	@Override
	public InvoiceReturnSearchResponse invoiceReturnViewDetail(SearchInvoiceReturnRequest request, String userCode) {
		return dao.invoiceReturnViewDetail(request,userCode);
	}


	@Override
	public JasperPrint PdfGeneratorReportForInvoiceReturn(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		 return dao.PdfGeneratorReportForJobCardOpenDao(request,jasperName,jasperParameter,filePath);
		
		
	}


	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) {
		  try {
			dao.printReport(jasperPrint,format,printStatus,outputStream,reportName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		
		return dao.invoiceReturnExcelReport(request,jasperName,jasperParameter,filePath);
	}

	
	@Override
	public void printInvoiceExcelReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) {
		  try {
			dao.printInvoiceExcelReport(jasperPrint,format,printStatus,outputStream,reportName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public List<partSearchResponseModel> fetchPartNumber(String userCode, String searchText) {
		return dao.fetchPartNumber(userCode,searchText);
	}


	@Override
	public VstWithoutRefPartDtl getInvoiceReturnPartDetails(String userCode, BigInteger partId) {
		return dao.getInvoiceReturnPartDetails(userCode,partId);
	}


	@Override
	public List<PartyInvoiceList> fetchInvoiceReturnNumList(String userCode, InvoicePartNoRequest requestModel) {
		return dao.fetchInvoiceReturnNumList(userCode,requestModel);
	}

	


	
	
	
	
}
