package com.hitech.dms.web.Controller.report.partGrnSearch;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.web.model.report.model.PartGrnSearchList;
import com.hitech.dms.web.model.report.model.PartGrnSearchRequest;
import com.hitech.dms.web.service.report.partGrnSearch.PartGrnSearchService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("spare/partGrnReport")
@SecurityRequirement(name = "hitechApis")
public class PartGrnSearchController {
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	private PartGrnSearchService partGrnSearchService;
	
	@Value("${file.upload-dir.partGrnTemplate}")
    private String partInvoiceExcelPath;
	
	
	@PostMapping(value="/partGrnReportSearch")
	public ResponseEntity<?> partGrnReportSearch(@RequestBody PartGrnSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		System.out.println();
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		PartGrnSearchList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Part GRN report not search or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = partGrnSearchService.partGrnSearch(userCode, resquest,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Part GRN Search report.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	 }
	
	
	@PostMapping("/partGrnReportExcel")
	public void partGrnReportExcel(@RequestBody PartGrnSearchRequest resquest, @RequestParam(defaultValue = "xlsx") String format,
		@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
		HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		String[] cu =null;
		if(resquest.getCustomerName()!=null) {
		 cu = resquest.getCustomerName().split("\\|");
		}
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("dealerName", cu!=null?cu[1].trim():null);
		jasperParameter.put("partNo", resquest.getPartNumber());
		jasperParameter.put("partCatId", resquest.getPartCategoryId());
		jasperParameter.put("fromDate",resquest.getFromDate());
		jasperParameter.put("toDate",resquest.getToDate());
		jasperParameter.put("page", resquest.getPage());
		jasperParameter.put("size", resquest.getSize());
		jasperParameter.put("branchId", resquest.getBranchId());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=partInvoiceExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Part-Grn/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "PartGRNReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=PartGRNReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream,null);

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
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream, String reportName)
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
