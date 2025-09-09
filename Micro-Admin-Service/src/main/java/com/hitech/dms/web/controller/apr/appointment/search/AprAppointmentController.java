package com.hitech.dms.web.controller.apr.appointment.search;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.hitech.dms.web.model.admin.apr.request.AprAppointmentSearchRequest;





@RestController
@RequestMapping("/api/v1/aprAppointment")
public class AprAppointmentController {
	
	

//	private SimpleDateFormat getSimpleDateFormat() {
//		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//	}
//	
//	@Autowired
//	private ConnectionConfiguration dataSourceConnection;
//	
//
//	@Value("${file.upload-dir.aprAppointmentTemplate}")
//    private String aprAppointmentExcelPath;
//	
//	
//	
//	@PostMapping("/aprAppointmentExcel")
//	public void aprAppointmentExcel(@RequestBody AprAppointmentSearchRequest req, @RequestParam(defaultValue = "xlsx") String format,
//			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		OutputStream outputStream = null;
//		String userCodes = null;
//		if (authentication != null) {
//			userCodes = authentication.getUserAuthentication().getName();
//		}
//		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
//			
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		
//		jasperParameter.put("userCode", userCodes);
//		jasperParameter.put("fromDate", formatter.format(req.getFromDate()));
////		jasperParameter.put("toDate", formatter.format(DateToStringParserUtils.addDayByOne(req.getToDate())));
//		jasperParameter.put("partyTypeId", req.getPartyTypeId());
//		jasperParameter.put("branchId", req.getBranchId());
//		jasperParameter.put("partyCodeId", req.getPartyCodeId());
//		jasperParameter.put("invoiceId", req.getInvoiceId());
//		jasperParameter.put("aprReturnNumber", req.getAprReturnNumber());
//		jasperParameter.put("page", req.getPage());
//		jasperParameter.put("size", req.getSize());
//	   
//		
//		
//		String filePath="";
//		String property = System.getProperty("os.name");
//		
//		if(property.contains("Windows")) {
//			filePath=aprAppointmentExcelPath;
//		}else {
//			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Apr-Appointment/Excel/";
//		}
//		
//		JasperPrint jasperPrint = xlsxGeneratorReport(request, "APRReturnExcel.jasper" , jasperParameter,filePath);
//		try {
//			response.setContentType("application/xlsx");
//			response.setHeader("Content-Disposition", "inline; filename=APRReturnExcel.xlsx");
//			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
//			outputStream = response.getOutputStream();
//			printReport(jasperPrint, format, printStatus, outputStream,null);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (outputStream != null) {
//				outputStream.flush();
//				outputStream.close();
//			}
//		}
//	}
//	
//	public JasperPrint xlsxGeneratorReport( 
//			  HttpServletRequest request, String jaspername,
//			  HashMap<String, Object> jasperParameter,String downloadPath) {
//		
////		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
//		JasperPrint jasperPrint = null;
//		Connection connection = null;
//		try {
//			String filePath=downloadPath+jaspername;
//			System.out.println("filePath  "+filePath);
//			connection = dataSourceConnection.getConnection();
//        System.out.println("jasperParameter "+jasperParameter);
//
//        if (connection != null) {
//				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);
//			}
//		} catch (Exception e) {
//			jasperPrint = null;
//			e.printStackTrace();
//		} finally {
//			try {
//				if (connection != null)
//					connection.close();
//			} catch (Exception e) {
//				jasperPrint = null;
//				e.printStackTrace();
//			}
//		}
//		return jasperPrint;
//	}
//	
//
//	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream, String reportName)
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
//	
//	
//	public void printPDFReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
//			throws Exception {
//
//		JRPdfExporter exporter = new JRPdfExporter();
//
//		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//
//		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
//		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//		if (printStatus != null && printStatus.equals("true")) {
//			configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
//			configuration.setPdfJavaScript("this.print();");
//		}
//		exporter.setConfiguration(configuration);
//		exporter.exportReport();
//	}

}
