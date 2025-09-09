package com.hitech.dms.web.service.sale.invoiceReturn;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

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

import net.sf.jasperreports.engine.JasperPrint;

public interface SpareSaleInvoiceReturnService {
	
	
	
	public InvoiceReturnDetail getInvoiceDetailByDOcNo(String mrnNo,String userCode);
	public InvoiceReturnDetail getInvoiceDetailByClaimGeneration(String mrnNo,String userCode);
	JasperPrint PdfGeneratorReportForInvoiceReturn(HttpServletRequest request, String jasperName,HashMap<String, Object> jasperParameter, String filePath);
	JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jasperName,HashMap<String, Object> jasperParameter, String filePath);
	public InvoiceReturnDetailSaveResponse saveInvoiceReturnDetail(InvoiceReturnRequestModel requestData,String userCode);
	public List<SpareInvoiceRetunSearchModel> searchInvoiceReturn(String searchType,String searchText,String userCode);
	public InvoiceReturnApproveStatus updateInvoiceReturnStatus(InvoiceReturnRequestModel request,String userCode);
	public InvoiceReturnSearchResponse searchInvoiceReturnByDate(SearchInvoiceReturnRequest request,String userCode);
	public UploadDisagreeDocumentModel uploadDisagreeDocument(UploadDisagreeDocumentModel request,String userCode);
	public InvoiceReturnSearchResponse  invoiceReturnViewDetail(SearchInvoiceReturnRequest request,String userCode);

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);
	
	public void printInvoiceExcelReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);
	public List<partSearchResponseModel> fetchPartNumber(String userCode, String searchText);
	public VstWithoutRefPartDtl getInvoiceReturnPartDetails(String userCode, BigInteger partId);
	public List<PartyInvoiceList> fetchInvoiceReturnNumList(String userCode, InvoicePartNoRequest requestModel);
	
	
}
