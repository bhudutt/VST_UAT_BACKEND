package com.hitech.dms.web.Controller.spare.sale.aprReturn;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
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
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnSearchRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.CreateAprReturnRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnNumber;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.InvoicePartDetailResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.SpareAprRetunCreateResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnResponse;
import com.hitech.dms.web.service.sale.aprReturn.AprReturnService;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/v1/aprReturn")
public class AprReturnController {
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	
	@Value("${file.upload-dir.aprReturnTemplate}")
    private String aprReturnExcelPath;
	
	
	@Value("${file.upload-dir.aprPdfTemplate}")
    private String aprPdfPath;
	
	@Autowired
	private AprReturnService aprReturnService;
	
	
	
	@GetMapping("/fetchPartyInvoiceList")
	public ResponseEntity<?> fetchPartyInvoiceList(@RequestParam(name="partyBranchId") Integer partyBranchId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<PartyInvoiceList> responseList = aprReturnService.fetchPartyInvoiceList(partyBranchId, userCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Refernce Doc List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Party Invoice List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Party Invoice List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/getAprReturnPartDetail")
	public ResponseEntity<?> getInvoicePartDetail(@RequestParam(name="saleInvoiceId") Integer saleInvoiceId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<InvoicePartDetailResponse> responseList = aprReturnService.getInvoicePartDetail(saleInvoiceId, userCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Refernce Doc List on " + LocalDate.now());
			codeResponse.setMessage("Apr return part List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr return part List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/create")
	public ResponseEntity<?> createAprReturn(@Valid @RequestBody CreateAprReturnRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareAprRetunCreateResponse responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare apr return not created or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		 responseModel = aprReturnService.createAprReturn(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/fetchAprReturnNo")
	public ResponseEntity<?> fetchAprReturnNoList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody InvoicePartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<AprReturnNumber> responseModelList = aprReturnService.fetchAprReturnNoList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Apr return number List on " + LocalDate.now());
			codeResponse.setMessage("Apr return Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr return Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/fetchInvoiceNum")
	public ResponseEntity<?> fetchInvoiceNumList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody InvoicePartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<PartyInvoiceList> responseModelList = aprReturnService.fetchInvoiceNumList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Apr return number List on " + LocalDate.now());
			codeResponse.setMessage("Apr return Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr return Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/partyCodeList")
	public ResponseEntity<?> partyCodeList(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCategoryResponse> responseModel = aprReturnService.aprPartyCodeList(searchText, userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	@PostMapping(value="/search")
	public ResponseEntity<?> aprReturnSearchList(@RequestBody AprReturnSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		AprReturnSearchList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr Return not search or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = aprReturnService.search(userCode, resquest,device);
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
	
	@SuppressWarnings("unchecked")
	@GetMapping("/viewByAprReturnId")
	public ResponseEntity<?> viewAprReturnDetail(@RequestParam(name = "aprReturnId") BigInteger aprReturnId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	   ViewAprReturnResponse responseModel = aprReturnService.viewAprReturnDetail(aprReturnId,userCode);
		
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successfully" +LocalDate.now());
			codeResponse.setMessage("View Apr Return Details");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " +LocalDate.now());
			codeResponse.setMessage("View Apr Return Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/excel")
	public void generateReport(@RequestBody AprReturnSearchRequest req, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("fromDate", formatter.format(req.getFromDate()));
		jasperParameter.put("toDate", formatter.format(DateToStringParserUtils.addDayByOne(req.getToDate())));
		jasperParameter.put("partyTypeId", req.getPartyTypeId());
		jasperParameter.put("branchId", req.getBranchId());
		jasperParameter.put("partyCodeId", req.getPartyCodeId());
		jasperParameter.put("invoiceId", req.getInvoiceId());
		jasperParameter.put("aprReturnNumber", req.getAprReturnNumber());
		jasperParameter.put("page", req.getPage());
		jasperParameter.put("size", req.getSize());
	   
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=aprReturnExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Apr-Return/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "APRReturnExcel.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=APRReturnExcel.xlsx");
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
	
	@GetMapping("/viewPrint")
	public void generateGrnReport(@RequestParam(required = false) Integer aprReturnId, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Apr Return Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("ID", aprReturnId);
		jasperParameter.put("FLAG", null);

		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=aprPdfPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Apr-Return/Pdf/";
		}
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "APRReturn.jasper", jasperParameter,filePath);
		try {
			
			if(format !=null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
			}else if(format !=null && format.equalsIgnoreCase("xls")) {
				response.setContentType("application/xls");
			}
			
			response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			
			printPDFReport(jasperPrint, format, printStatus, outputStream);

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
	
	public void printPDFReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
			throws Exception {

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
	}
	
	
	@PostMapping(value="/searchAprAppointment")
	public ResponseEntity<?> aprAppointmentSearchList(@RequestBody AprReturnSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		AprAppointmentSearchList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr Appointment not search or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = aprReturnService.aprAppointmentSearch(userCode, resquest,device);
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
	

	@PostMapping(value="/searchAprMapping")
	public ResponseEntity<?> aprMappingSearchList(@RequestBody AprReturnSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		AprAppointmentSearchList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr Mapping not search or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = aprReturnService.aprMappingReportSearch(userCode, resquest,device);
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
	
	

	@PostMapping("/aprAppointmentExcel")
	public void aprAppointmentExcel(@RequestBody AprReturnSearchRequest req, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("fromDate", formatter.format(req.getFromDate()));
		jasperParameter.put("toDate", formatter.format(DateToStringParserUtils.addDayByOne(req.getToDate())));
		jasperParameter.put("partyTypeId", req.getPartyTypeId());
		jasperParameter.put("branchId", req.getBranchId());
		jasperParameter.put("partyCodeId", req.getPartyCodeId());
		jasperParameter.put("stateId", req.getStateId());
		jasperParameter.put("districtId", null);
		jasperParameter.put("status", req.getStatus());
		jasperParameter.put("page", req.getPage());
		jasperParameter.put("size", req.getSize());
	  
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=aprReturnExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Apr-Return/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "APRAppointedMonthReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=APRAppointedMonthReport.xlsx");
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
	
	
	@PostMapping("/exportAprMappingExcel")
	public void exportAprMappingExcel(@RequestBody AprReturnSearchRequest req, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("fromDate", formatter.format(req.getFromDate()));
		jasperParameter.put("toDate", formatter.format(DateToStringParserUtils.addDayByOne(req.getToDate())));
		jasperParameter.put("partyTypeId", req.getPartyTypeId());
		jasperParameter.put("branchId", req.getBranchId());
		jasperParameter.put("partyCodeId", req.getPartyCodeId());
		jasperParameter.put("stateId", req.getStateId());
		jasperParameter.put("districtId", null);
		jasperParameter.put("status", req.getStatus());
		jasperParameter.put("page", req.getPage());
		jasperParameter.put("size", req.getSize());
	  
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=aprReturnExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Apr-Return/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "APRAppointedMappingReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=APRAppointedMappingReport.xlsx");
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
	
	

	@PostMapping(value="/cancelAprReturn")
	public ResponseEntity<?> cancelAprReturn(@RequestBody AprReturnCancelRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		SpareAprRetunCreateResponse responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Apr Return not cancel or server side error.");

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = aprReturnService.cancelAprReturn(userCode, resquest,device);
		if (responseModel != null && responseModel.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage("Apr Return cancel successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Cancel Apr Return");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	 }
	

}
