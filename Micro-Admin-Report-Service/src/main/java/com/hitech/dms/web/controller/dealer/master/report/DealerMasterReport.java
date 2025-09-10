package com.hitech.dms.web.controller.dealer.master.report;

import java.io.IOException;
import java.io.OutputStream;
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

import com.hitech.dms.web.dao.adminReport.AdminReportDao;
import com.hitech.dms.web.model.admin.DealerMaster;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;
/**
 * @author Vivek.Gupta
 *
 */

@RestController
@RequestMapping("/dealerMasterReport")
@SecurityRequirement(name = "hitechApis")
public class DealerMasterReport {
	
	
	@Autowired
    private AdminReportDao adminDao;
	
	@Value("${file.upload-dir.DealerMasterTemp}")
    private String dmExcelPath;
	
	
	@PostMapping("/exportDMReports")
	public void generateReport(@RequestBody DealerMaster requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		String reportName = "Dealer Master Report";
		jasperParameter.put("UserCode", userCodes);
		jasperParameter.put("DealerCode", requestModel.getDealerCode());
		jasperParameter.put("page", null);
		jasperParameter.put("size", null);
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=dmExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/dealer-master/Excel/";
		}
		
		JasperPrint jasperPrint = adminDao.ExcelGeneratorReport(request, "DealerReport.jasper" , jasperParameter,filePath);
		
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=DealerReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			adminDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
//            System.out.println("jasperParameter "+jasperParameter);
//
//            if (connection != null) {
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

}
