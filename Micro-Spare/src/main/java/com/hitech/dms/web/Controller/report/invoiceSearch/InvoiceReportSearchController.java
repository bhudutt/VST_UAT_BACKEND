package com.hitech.dms.web.Controller.report.invoiceSearch;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartCustResponse;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchList;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.ZoneResponse;
import com.hitech.dms.web.service.report.invoiceSearch.InvoiceReportService;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/v1/partInvoice")
@Validated
public class InvoiceReportSearchController {
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	

	@Value("${file.upload-dir.partInvoiceTemplate}")
    private String partInvoiceExcelPath;
	
	@Autowired
	private InvoiceReportService invoiceReportService;

	@PostMapping(value="/search")
	public ResponseEntity<?> searchByDcFields(@RequestBody InvoiceReportSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		InvoiceReportSearchList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Part invoice report not search or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = invoiceReportService.search(userCode, resquest,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Part invoice report.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	 }
	
	
	
	@PostMapping("/partNumber")
	public ResponseEntity<?> fetchPartNumberList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody InvoicePartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<partSearchResponseModel> responseModelList = invoiceReportService.fetchPartNumber(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + LocalDate.now());
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/kpdList")
	public ResponseEntity<?> kpdList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody InvoicePartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<KPDResponse> responseModel = invoiceReportService.getKPDList(userCode,requestModel);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " );
			codeResponse.setMessage("KDP Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("KDP Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/zoneList")
	public ResponseEntity<?> zoneList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<ZoneResponse> responseModel = invoiceReportService.getZoneList(userCode);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " );
			codeResponse.setMessage("Zone Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Zone Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/customerName")
	public ResponseEntity<?> fetchCustomerNameList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody InvoicePartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<InvoicePartCustResponse> responseModelList = invoiceReportService.fetchCustomerName(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + LocalDate.now());
			codeResponse.setMessage("Customer Name Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Customer Name Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	@PostMapping("/excel")
	public void generateReport(@RequestBody InvoiceReportSearchRequest resquest, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
//		String reportName="Delivery Challan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
//		query.setParameter("page", resquest.getPage());
//		query.setParameter("size", resquest.getSize());
		String[] cu =null;
		if(resquest.getCustomerName()!=null) {
		 cu = resquest.getCustomerName().split("\\|");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("FromDate", formatter.format(resquest.getFromDate()));
		jasperParameter.put("ToDate", formatter.format(DateToStringParserUtils.addDayByOne(resquest.getToDate())));
		jasperParameter.put("UserCode", userCodes);
		jasperParameter.put("page", resquest.getPage());
		jasperParameter.put("size", resquest.getSize());
		jasperParameter.put("zone", resquest.getZone());
		jasperParameter.put("stateId", resquest.getStateId());
		jasperParameter.put("PartNo", resquest.getPartNumber());
		jasperParameter.put("partCatId", resquest.getPartCategoryId()!=null?resquest.getPartCategoryId().intValue():null);
		jasperParameter.put("customerName", cu!=null?cu[1].trim():null);
		jasperParameter.put("kpdId", resquest.getKpdId());
		jasperParameter.put("custFlag", resquest.getCustFlag());
		jasperParameter.put("branchId", resquest.getBranchId()!=null?resquest.getBranchId().intValue():null);
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=partInvoiceExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Part-Invoice/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "InvoicePartReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=InvoicePartReport.xlsx");
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
