package com.hitech.dms.web.controller.sparerequisition.print.controller;

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
import com.hitech.dms.web.model.partrequisition.search.request.PartRequisitionSearchRequestModel;
import com.lowagie.text.pdf.PdfWriter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/workshopRequisition")
@SecurityRequirement(name = "hitechApis")
public class SpareRequisitionPrintController {

	private static final Logger logger = LoggerFactory.getLogger(SpareRequisitionPrintController.class);
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.RequisitionReports}")
    private String downloadRequisitionPath;
	@Value("${file.upload-dir.RequisitionPrint}")
	private String downloadPrinrRequisition;
	
	@PostMapping("/exportRequisitionReports")
	public void generateReport(@RequestBody PartRequisitionSearchRequestModel requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Requisition Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("UserCode", userCodes);
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate",requestModel.getToDate());
		jasperParameter.put("JobCardNo",requestModel.getJobCardNo());
		jasperParameter.put("RequisitionNumber", requestModel.getRequisitionNo());
		jasperParameter.put("RequisitionType", requestModel.getRequisitionType());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		jasperParameter.put("pcId", requestModel.getProfitCenterId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierarchyId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("Stateid", requestModel.getStateId());
		jasperParameter.put("ZoneId", requestModel.getZone());
		jasperParameter.put("TerritoryId", requestModel.getTerritory());
				
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadRequisitionPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Requisition/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "sparespartrequisition.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=RequisitionReports.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream,reportName);

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

	public JasperPrint xlsxGeneratorReport( 
			  HttpServletRequest request, String jaspername,
			  HashMap<String, Object> jasperParameter,String downloadPath) {
		
//		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
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

//	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
//			throws Exception {
//
//		JRXlsxExporter exporter = new JRXlsxExporter();
//		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
//        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
//        exporter.setConfiguration(reportConfigXLS);
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
//       
//        exporter.exportReport();
//	}

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception {

		if (format != null && format.equalsIgnoreCase("pdf")) {
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

		} else if (format != null && format.equalsIgnoreCase("xlsx")) {

			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

			exporter.exportReport();
		}
	}
	
	
	@GetMapping("/exportRequisitionPrint")
	public void generateReqPrint(@RequestParam(value = "requsitonId") Integer requsitonId, @RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Requisition Print";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("RequisitionId", requsitonId);
		jasperParameter.put("flag", 1);

				
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadPrinrRequisition;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/RequsitonPrint/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "sparespartrequisitionfront.jasper" , jasperParameter,filePath);
		try {
			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			printReport(jasperPrint, format, printStatus, outputStream,reportName);

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
