package com.hitech.dms.web.controller.allotment.deallot.docs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.dao.dc.search.DcSearchDao;
import com.hitech.dms.web.model.activity.search.request.ActivitySearchDetailsExportModel;
import com.hitech.dms.web.model.allot.create.request.MachineAllotCreateRequestModel;
import com.hitech.dms.web.model.allot.search.request.MachineAllotSearchRequestModel;
import com.hitech.dms.web.model.gstinvoice.list.request.GstInvoiceFilterRequest;
/*import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;*/
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


@RestController
@RequestMapping("/export")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	private DcSearchDao searchDao;

	
	@Value("${file.upload-dir.Report}")
    private String downloadPath;
	
	
	@Value("${file.upload-dir.Reporttwo}")
    private String deliveryChallanDownloadPath;
	
	@Value("${file.upload-dir.Reportthree}")
    private String machineInvoiceDownloadPath;
	
	@Value("${file.upload-dir.ReportActivitySearch}")
    private String activitySearchdownloadPath;
	
	@Value("${file.upload-dir.ReportActivityPlanSearch}")
    private String reportActivityPlanSearch;
	
	@Value("${file.upload-dir.ReportPlanApproval}")
    private String reportPlanApproval;
	
	@Value("${file.upload-dir.ReportQuotationval}")
    private String quotationFilePath;
	
	@Value("${file.upload-dir.gstInvoice}")
    private String gstInvoice;
	
	@Value("${file.upload-dir.vstUserPath}")
	private String vstUserPath;
	
	@Value("${file.upload-dir.orgHierPath}")
	private String orgHierPath;
	
	
	
	
	@PostMapping("/exportActivityPlanSearchDetail")
	public void exportActivityPlanSearchDetail(@RequestBody ActivitySearchDetailsExportModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Plan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activity_number", requestModel.getActivityNo());
		jasperParameter.put("includeInactive", requestModel.getIncludeInactive());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("page", requestModel.getPage());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportActivityPlanSearch;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/activity plan detail/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivityPlanDetail.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	
	
	@PostMapping("/exportSalesOrderStockOverAllReports")
	public void exportSalesOrderStockOverAllReports(@RequestBody ActivitySearchDetailsExportModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Plan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activity_number", requestModel.getActivityNo());
		jasperParameter.put("includeInactive", requestModel.getIncludeInactive());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("page", requestModel.getPage());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportActivityPlanSearch;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/activity plan detail/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivityPlanDetail.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportSalesOrderStockTransitReports")
	public void exportSalesOrderStockTransitReports(@RequestBody ActivitySearchDetailsExportModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Plan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activity_number", requestModel.getActivityNo());
		jasperParameter.put("includeInactive", requestModel.getIncludeInactive());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("page", requestModel.getPage());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportActivityPlanSearch;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/activity plan detail/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivityPlanDetail.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@GetMapping("/exportMachineInvoice")
	public void exportMachineInvoice(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Machine Invoice Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("invoiceId", Integer.parseInt(id));
		jasperParameter.put("flag", 1);
		

		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=machineInvoiceDownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/machine invoice/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Product_Invoice_FrontPage.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportActivitySearchDetail")
	public void exportActivitySearchDetail(@RequestBody ActivitySearchDetailsExportModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Search Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activity_number", requestModel.getActivityNo());
		jasperParameter.put("includeInactive", requestModel.getActivityNo());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=activitySearchdownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/activity search detail/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivitySearchDetail.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportAllAllotment")
	public void exportAllAllotment(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Allotment Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("pcid", requestModel.getPcId());
		jasperParameter.put("dealerId", requestModel.getDealerId() !=null ? requestModel.getDealerId().intValue() : null);
		jasperParameter.put("branchId", requestModel.getBranchId() !=null ? requestModel.getBranchId().intValue() : null);
		jasperParameter.put("AllotNo", requestModel.getAllotNumber());
		jasperParameter.put("EnquiryNo", requestModel.getEnquiryNo());
		jasperParameter.put("series", requestModel.getSeries());
		jasperParameter.put("segment", requestModel.getSegment());
		jasperParameter.put("model", requestModel.getModel());
		jasperParameter.put("variant", requestModel.getVariant());
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("zone", requestModel.getZone());
		jasperParameter.put("Area", requestModel.getArea());
		jasperParameter.put("AllotmentId", requestModel.getAllotmentId());
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Allotment/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Allotment_Report.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@GetMapping("/exportAllotmentDeallot")
	public void generateReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Allotment Deallot Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("usercode", userCode);
		jasperParameter.put("allotmentNo", "");
		jasperParameter.put("allotmentId", Integer.parseInt(id));
		jasperParameter.put("flag", 1);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Allotment/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Allotment_invoice.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@GetMapping("/exportDeliveryChallan")
	public void generateDeliveryChallanReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Delivery Challan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("dcId", Integer.parseInt(id));
		jasperParameter.put("flag", 1);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=deliveryChallanDownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Delivery Challan/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "Product_Delivery_Challan_FrontPage.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@PostMapping("/exportAllDeliveryChallan")
	public void exportAllDeliveryChallan(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Delivery Challan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PcID", requestModel.getPcId());
		jasperParameter.put("Stateid", requestModel.getStateId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID1());
		jasperParameter.put("dealerId", requestModel.getDealerId1());
		jasperParameter.put("BranchId", requestModel.getBranchId1());
		jasperParameter.put("dcNumber", requestModel.getDcNumber());		
		jasperParameter.put("AllotNo", requestModel.getAllotNumber());
		jasperParameter.put("EnquiryNo", requestModel.getEnquiryNo());
		jasperParameter.put("Series", requestModel.getSeries());
		jasperParameter.put("Segment", requestModel.getSegment());
		jasperParameter.put("Model", requestModel.getModel());
		jasperParameter.put("variant", requestModel.getVariant());
		jasperParameter.put("DCStatus", requestModel.getDcStatus());		
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=deliveryChallanDownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Delivery Challan/";
		}
		System.out.println("exportAllDeliveryChallan "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Delivery_Challan.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@GetMapping("/exportPlanApproval")
	public void exportPlanApproval(@RequestParam(required = false) String planHdrId, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Plan Approval Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("activity_plan_hdr_id", planHdrId);
		jasperParameter.put("usercode", userCode);
		

		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportPlanApproval;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/plan approval/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Plan_Approvals.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportAllMachineInvoiceData")
	public void exportAllMachineInvoiceData(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Machine Invoice Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PcID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID() !=null?requestModel.getOrgHierID().intValue():null);
		jasperParameter.put("dealerId", requestModel.getDealerId() !=null ? requestModel.getDealerId().intValue():null);
		jasperParameter.put("BranchId", requestModel.getBranchId() !=null ? requestModel.getBranchId().intValue():null);
		jasperParameter.put("dcNumber", requestModel.getDcNumber());	
		jasperParameter.put("invoiceNumber", requestModel.getAllotNumber());
		jasperParameter.put("poNumber", requestModel.getEnquiryNo());
		jasperParameter.put("Series", requestModel.getSeries());
		jasperParameter.put("Segment", requestModel.getSegment());
		jasperParameter.put("Model", requestModel.getModel());
		jasperParameter.put("variant", requestModel.getVariant());
		jasperParameter.put("invoiceStatus", requestModel.getDcStatus());		
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=machineInvoiceDownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/machine invoice/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Machine_Invoice.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}

	public JasperPrint pdfGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter,String downloadPath) {
		//String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			//String filePath = ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();
			
			String filePath=downloadPath+jaspername;
			System.out.println("filePath  "+filePath);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}
	
	
	@PostMapping("/exportStockErrorReports")
	public void exportStockErrorReports(@RequestBody ActivitySearchDetailsExportModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Stock Error Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("USERCODE", userCode);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=quotationFilePath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Quotation/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "StockErrorReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);
			
			searchDao.deleteStockErrorHistory();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,String reportName)
			throws Exception {

		
		if(format !=null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}else if(format != null && format.equalsIgnoreCase("xls")) {
			
			
			
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
	       
	        exporter.exportReport();
			
			
			
			/*
			 * JRXlsExporter xlsExporter = new JRXlsExporter();
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.ignore.graphics",
			 * "false"); //
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.sheet.name", //
			 * "true");
			 * 
			 * File file = new File(reportName + ".xls");
			 * 
			 * xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			 * 
			 * SimpleXlsReportConfiguration configuration = new
			 * SimpleXlsReportConfiguration(); configuration.setOnePagePerSheet(true);
			 * configuration.setDetectCellType(true);
			 * configuration.setCollapseRowSpan(false);
			 * configuration.setWhitePageBackground(false);
			 * configuration.setRemoveEmptySpaceBetweenColumns(true);
			 * configuration.setRemoveEmptySpaceBetweenRows(true);
			 * configuration.setIgnorePageMargins(true);
			 * 
			 * String sheetName[] = { reportName }; configuration.setSheetNames(sheetName);
			 * 
			 * xlsExporter.setConfiguration(configuration);
			 * xlsExporter.setExporterOutput(new
			 * SimpleOutputStreamExporterOutput(outputStream)); xlsExporter.exportReport();
			 */
		}
		
		
		
	}
	
	
	@GetMapping("/exportQuotation")
	public void exportQuotation(@RequestParam(required = false) Integer quotationHdrId, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Quotation Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("id", quotationHdrId);
		
		

		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=quotationFilePath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Quotation/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Servicre_Quatation.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@PostMapping("/exportAllPlanApproval")
	public void exportAllApproval(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("pc "+requestModel.getPcId());
		System.out.println("requestModel.getActivityNo() "+requestModel.getActivityNo());
		System.out.println("requestModel.requestModel.getFromDate1()() "+requestModel.getFromDate1());
		System.out.println("requestModel.getToDate1() "+requestModel.getToDate1());
		String reportName="Plan Approval Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("activity_plan_hdr_id", requestModel.getActivity_plan_hdr_id());
		jasperParameter.put("usercode", userCode);
		jasperParameter.put("Fromdate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("ActivityNo", requestModel.getActivityNo());
		
		
		String fileName="Plan_Approvals_detail.jasper";
		if(requestModel.getPcId() !=null && requestModel.getPcId()==1) {
			fileName="Plan_Approvals_detail.jasper";
			jasperParameter.put("Pcid", requestModel.getPcId());
		} 
		else if(requestModel.getPcId() !=null &&requestModel.getPcId()==2) {
			  fileName="Plan_Approvals_detail1.jasper"; jasperParameter.put("Pcid",
			  requestModel.getPcId()); 
		}
		else { fileName="Plan_Approvals_detail.jasper";
			  jasperParameter.put("Pcid", requestModel.getPcId()); 
		}
			 
		
				
		/*
		 * jasperParameter.put("page", requestModel.getPage());
		 * jasperParameter.put("size", requestModel.getSize());
		 */
		
	
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportPlanApproval;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/plan approval/";
		}
		System.out.println("jasperParameter  "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, fileName, jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/activityPlanVsActual")
	public void activityPlanVsActual(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Plan Vs Actual Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("actualActivityHdrId", requestModel.getActualActivityHdrId());
		jasperParameter.put("Fromdate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("ActivityNo", requestModel.getActivityNo());
				
		/*
		 * jasperParameter.put("page", requestModel.getPage());
		 * jasperParameter.put("size", requestModel.getSize());
		 */
		
	
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=reportPlanApproval;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/plan approval/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "Activityplan_vs_Actuals.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportAllGstInvoice")
	public void exportAllGstInvoice(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="GST Invoice Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activityClaimInvNumber",requestModel.getInvoiceNumber());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("Stateid", requestModel.getStateId());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=gstInvoice;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/gst invoice/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "actclaimgstinvexportall.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@PostMapping("/exportActivityClaim")
	public void exportActivityClaim(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Activity Claim Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchID", requestModel.getBranchId());
		jasperParameter.put("activityClaimNumber",requestModel.getInvoiceNumber());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=gstInvoice;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/gst invoice/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivityClaimApprovalReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@PostMapping("/vstUserReport")
	public void vstUserReport(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="VST User Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("employeeCode", requestModel.getEmployeeCode());
		jasperParameter.put("employeeName", requestModel.getEmployeeName());
		jasperParameter.put("fromDate", requestModel.getFromDate());
		jasperParameter.put("toDate", requestModel.getToDate());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=vstUserPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/vst user/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "VSTUserReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	
	
	
	@PostMapping("/vstUserReportNew")
	public void vstUserReportNew(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="VST User Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("employeeCode", requestModel.getEmployeeCode());
		jasperParameter.put("employeeName", requestModel.getEmployeeName());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("Page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=vstUserPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/vst user/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "EmployeeSearchVST.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportSCIReport")
	public void exportSCIReport(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="SCI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("ChassisNumber", requestModel.getChassisNo());
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());	
		jasperParameter.put("Page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=vstUserPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/vst user/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "SCIReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	
	@PostMapping("/exportWCIReport")
	public void exportWCIReport(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="WCI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("ChassisNumber", requestModel.getChassisNo());
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate", requestModel.getToDate());	
		jasperParameter.put("Page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=vstUserPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/vst user/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "WCIReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/fieldOrgReport")
	public void fieldOrgReport(@RequestBody GstInvoiceFilterRequest requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Field Org Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("depatmentid", requestModel.getDeptId());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=orgHierPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/field org/";
		}
		System.out.println("exportAllGST Invoice "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "FieldOrganizationReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
	@PostMapping("/exportAllDigitalDump")
	public void exportAllDigitalDump(@RequestBody MachineAllotSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="DEGITAL ENQUIRY REPORT";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("Usercode", userCode);
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("stateId", null);
		jasperParameter.put("zone", null);
		jasperParameter.put("digitalEnqSource", requestModel.getPlateFormId());	
		jasperParameter.put("fromDate", requestModel.getFromDate1());
		jasperParameter.put("toDate", requestModel.getToDate1());
		jasperParameter.put("status", null);		
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=deliveryChallanDownloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Delivery Challan/";
		}
		System.out.println("exportAllDegitalenq dump "+jasperParameter.toString());
		JasperPrint jasperPrint = pdfGeneratorReport( request, "DEGITALENQUIRYREPORT.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}
			
			
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}      
	}
	
}
