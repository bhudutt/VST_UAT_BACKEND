package com.hitech.dms.web.dao.pdi.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
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
import com.hitech.dms.web.controller.installation.dto.InstallationSearchDto;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchDto;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.grn.create.request.SalesGrnCreateRequestModel;
import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchRequestModel;
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
@RequestMapping("/pdi")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Value("${file.upload-dir.Report}")
    private String downloadPath;
	
	@Value("${file.upload-dir.Reports}")
    private String downloadPathStock;
	
	@Value("${file.upload-dir.ReportTwo}")
    private String downloadPathTwo;
	
	@Value("${file.upload-dir.ReportThree}")
    private String downloadPathThree;
	
	@Value("${file.upload-dir.ReportFour}")
    private String downloadPathFour;
	
	@Value("${file.upload-dir.ReportFive}")
    private String downloadPathFive;
	
	
	
	
	@PostMapping("/exportSalesOrderStockOverAllReports")
	public void exportSalesOrderStockOverAllReports(@RequestBody MachineStockSearchRequestModel requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Stock OverAll Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("Usercode", userCodes);
		jasperParameter.put("DealerCode", requestModel.getDealerCode());
		jasperParameter.put("branchId", requestModel.getBranchID());
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("Fromdate", requestModel.getFromDate());
		jasperParameter.put("Todate", requestModel.getToDate());
		jasperParameter.put("ProductDivision", requestModel.getProductDivision());
		jasperParameter.put("model", requestModel.getModelId());
		jasperParameter.put("ItemNo", requestModel.getItemNo());
		jasperParameter.put("includeInactive", requestModel.getIncludeInactive());
		jasperParameter.put("orgId", requestModel.getOrgId());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("chessisNo", requestModel.getChassisNo());
				
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathStock;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Stock/";
		}
		
		JasperPrint jasperPrint = pdfGeneratorReport(request, "ASONDATESTOCKREPORT.jasper" , jasperParameter,filePath);
		try {
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	
	@PostMapping("/exportAllMachinePO")
	public void generateAllMachinePOReport(@RequestBody GrnSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="PO Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("pcid", requestModel.getPcId());
		jasperParameter.put("orgId",  requestModel.getOrgHierID() !=null ?  requestModel.getOrgHierID().intValue() :null);		
		jasperParameter.put("dealerId",requestModel.getDealerId() !=null ? requestModel.getDealerId().intValue() : null);
		jasperParameter.put("branchId", requestModel.getBranchId() !=null ? requestModel.getBranchId().intValue() : null);
		jasperParameter.put("POFromDate", requestModel.getFromDate1());
		jasperParameter.put("POToDate", requestModel.getToDate1());
		jasperParameter.put("series", requestModel.getSeries());
		jasperParameter.put("segment", requestModel.getSegment());
		jasperParameter.put("model", requestModel.getModel());
		jasperParameter.put("variant", requestModel.getModel());
		jasperParameter.put("postatus", requestModel.getPostatus());
		jasperParameter.put("POon", requestModel.getPOon());
		jasperParameter.put("partyId", requestModel.getPartyId());
		jasperParameter.put("plantId", requestModel.getPlantId());
		jasperParameter.put("PONumber", requestModel.getPoNumber());
		jasperParameter.put("zone", requestModel.getZone());
		jasperParameter.put("Area", requestModel.getArea());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());
		jasperParameter.put("page",0);
		jasperParameter.put("size",0);
		jasperParameter.put("PoId", null);

		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathThree;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/machine po/";
		}
		
		
		JasperPrint jasperPrint = pdfGeneratorReport( request, "PO_Report.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	
	@GetMapping("/exportPurchaseReturnPrint")
	public void exportPurchaseReturnPrint(@RequestParam(required = false) String id, @RequestParam(required = false) String purchaseReturnNo,
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Purchase Return Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		
		
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("purchaseReturnId", Integer.valueOf(id));
		jasperParameter.put("purchaseReturnNo", purchaseReturnNo);
		jasperParameter.put("Flag", 0);
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathFour;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/purchase return/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "PurchaseReturnInvoicePrint.jasper", jasperParameter,filePath);
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
	
	
	@GetMapping("/exportMachinePO")
	public void generateMachinePOReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="PO Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("PoId", id);
		jasperParameter.put("Flag", "");
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathThree;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/machine po/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "po_invoice.jasper", jasperParameter,filePath);
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
	
	@PostMapping("/exportAllGRN")
	public void generateAllGrnReport(@RequestBody GrnSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Grn Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("Usercode", userCode);
		jasperParameter.put("PcId", requestModel.getPcId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID1());
		jasperParameter.put("DealerId",  requestModel.getDealerId1());
		jasperParameter.put("BranchId", requestModel.getBranchId1());
		jasperParameter.put("GrnNumber", requestModel.getGrnNo());
		jasperParameter.put("InvNumber", requestModel.getInvoiceNo());
		jasperParameter.put("Series", requestModel.getSeries());
		jasperParameter.put("Segment", requestModel.getSegment());
		jasperParameter.put("Model", requestModel.getModel());
		jasperParameter.put("variant", requestModel.getVariant());
		jasperParameter.put("GrnTypeId", requestModel.getGrnTypeId());
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("zone", requestModel.getZone());
		jasperParameter.put("Area", requestModel.getArea());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		System.out.println("jasperParameter  "+jasperParameter);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "GRN_Report.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	@GetMapping("/exportGRN")
	public void generateGrnReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Grn Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("Usercode", userCode);
		jasperParameter.put("GrnId", id);

		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "grn_invoice.jasper", jasperParameter,filePath);
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
	
	@GetMapping("/exportPdi")
	public void generateReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="PDI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		System.out.println("pdi No. "+id);
		jasperParameter.put("PDINo", id);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/pdi/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "PdiHeader.jasper", jasperParameter,filePath);
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
	
	
	@GetMapping("/exportOutwardPdi")
	public void exportOutwardPdi(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="OUTWARD PDI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		System.out.println("pdi No. "+id);
		jasperParameter.put("PDINo", id);
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/pdi/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "PdiOuter.jasper", jasperParameter,filePath);
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
System.out.println("jasperParameter "+jasperParameter);
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
	
	@GetMapping("/exportGSTInvoiceTax")
	public void exportGSTInvoiceTax(@RequestParam(required = false) Integer dealerId,
			@RequestParam(required = false) Integer activityId,
			@RequestParam(required = false) Integer stateCode,
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="GST INVOICE";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("dealerId", null);
		jasperParameter.put("ACTIVITYID", activityId);
		jasperParameter.put("FLAG", 1);
		jasperParameter.put("VST_State_Code", null);
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathFive;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/gst invoice/";
		}

		JasperPrint jasperPrint = pdfGeneratorReport( request, "ActivityClaimGstInvoiceFront.jasper", jasperParameter,filePath);
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
	
	@PostMapping("/exportAllPDI")
	public void generateAllPDIReport(@RequestBody GrnSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="PDI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("ChassisNo", requestModel.getChassisNo());
		jasperParameter.put("EngineNo", requestModel.getEngineNo());
		jasperParameter.put("Grnfromdate", null);
		jasperParameter.put("Grmtodate", null);
		jasperParameter.put("PDINumber", requestModel.getPdiNo());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("invoiceNumber",requestModel.getInvoiceNo() );
		jasperParameter.put("UserCode", userCode);
		System.out.println("jasperParameter  "+jasperParameter);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "PDIInwardExcel.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	@PostMapping("/exportAllOutwardPDI")
	public void exportAllOutwardPDI(@RequestBody GrnSearchRequestModel requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="OUTWARD PDI Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("FromDate", requestModel.getFromDate1());
		jasperParameter.put("ToDate", requestModel.getToDate1());
		jasperParameter.put("ChassisNo", requestModel.getChassisNo());
		jasperParameter.put("EngineNo", requestModel.getEngineNo());
		jasperParameter.put("Grnfromdate", null);
		jasperParameter.put("Grmtodate", null);
		jasperParameter.put("OutwardNumber", requestModel.getPdiNo());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("invoiceNumber",requestModel.getInvoiceNo() );
		
		System.out.println("jasperParameter  "+jasperParameter);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "PDIOutwardExcel.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	@PostMapping("/exportInstallation")
	public void exportInstallation(@RequestBody InstallationSearchDto requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="Installation Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("usercode", userCode);
		jasperParameter.put("status", requestModel.getStatus());
		jasperParameter.put("fromdate", requestModel.getFromDate());
		jasperParameter.put("todate", requestModel.getToDate());
		jasperParameter.put("chassisno", requestModel.getChassisNo());
		jasperParameter.put("installation_no", requestModel.getInstallationNo());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		

		System.out.println("jasperParameter  "+jasperParameter);
		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "InsDoneReport.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
	
	@PostMapping("/exportAllRCUpdate")
	public void exportAllRCUpdate(@RequestBody RegistrationSearchDto requestModel, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName="RC Update Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("ChassisNo", requestModel.getChassisNo());
		jasperParameter.put("RegistrationNo", requestModel.getRegistrationNo());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("selectedValue", requestModel.getSelectedValue());
		System.out.println("jasperParameter  "+jasperParameter);
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPathTwo;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/grn/";
		}
		JasperPrint jasperPrint = pdfGeneratorReport( request, "RCUpdateExcel.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				//response.setContentType("application/xls");
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
