package com.hitech.dms.web.controller.partreturn.create;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.model.partreturn.request.PartReturnSearchRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/partReturn")
@SecurityRequirement(name = "hitechApis")
public class SparePartReturnExcelExportController {

	private static final Logger logger = LoggerFactory.getLogger(SparePartReturnExcelExportController.class);
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Autowired
	@Value("${file.upload-dir.SpareReturnReports}")
    private String downloadSpareReturnPath;
	
	@PostMapping("/exportSpareReturnReports")
	public void generateReport(@RequestBody PartReturnSearchRequestModel requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="SpareReturn Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("UserCode", userCodes);
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("ToDate",requestModel.getTodate());
		jasperParameter.put("PartReturnNumber", requestModel.getPartReturnNo());
		jasperParameter.put("PartReturnType", requestModel.getReturnType());
		jasperParameter.put("page",requestModel.getPage());
		jasperParameter.put("size",requestModel.getSize());			
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadSpareReturnPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/SpareReturn/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "SparePartReturn.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=SparePartReturn.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream);

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
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
			throws Exception {

		JRXlsxExporter exporter = new JRXlsxExporter();
		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
       
        exporter.exportReport();
	}
}

