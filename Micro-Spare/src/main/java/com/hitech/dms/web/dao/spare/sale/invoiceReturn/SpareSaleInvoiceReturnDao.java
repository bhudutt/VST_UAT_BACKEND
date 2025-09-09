package com.hitech.dms.web.dao.spare.sale.invoiceReturn;

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

public interface SpareSaleInvoiceReturnDao {
	
	public InvoiceReturnDetail getInvoiceDetailByDOcNo(String mrnNo,String userCode);
	public InvoiceReturnDetail getInvoiceDetailByClaimGenerationNo(String mrnNo,String userCode);
	public InvoiceReturnDetailSaveResponse saveInvoiceReturnDetail(InvoiceReturnRequestModel requestData,String userCode);
	public List<SpareInvoiceRetunSearchModel> searchInvoiceReturnDao(String searchType,String searchText,String userCode);
	public InvoiceReturnApproveStatus updateInvoiceReturnStatus(InvoiceReturnRequestModel request,String userCode);
	public InvoiceReturnSearchResponse searchInvoiceReturnByDateDao(SearchInvoiceReturnRequest request,String userCode);
	public UploadDisagreeDocumentModel uploadDisagreeDocument(UploadDisagreeDocumentModel request,String userCode);
	
	public InvoiceReturnSearchResponse invoiceReturnViewDetail(SearchInvoiceReturnRequest request,String userCode);
	public JasperPrint PdfGeneratorReportForJobCardOpenDao(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath);
	public JasperPrint invoiceReturnExcelReport(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath);
	
	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;
	
	void printInvoiceExcelReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;
	public List<partSearchResponseModel> fetchPartNumber(String userCode, String searchText);
	public VstWithoutRefPartDtl getInvoiceReturnPartDetails(String userCode, BigInteger partId);
	public List<PartyInvoiceList> fetchInvoiceReturnNumList(String userCode, InvoicePartNoRequest requestModel);


}
