package com.hitech.dms.web.Controller.spare.deliveryChallan;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSearchRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateStatusRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DCcustomerOrderResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcPartQtyCalCulationResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchListResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanHeaderAndDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.SpareDcCreateResponse;
import com.hitech.dms.web.service.spare.deliveryChallan.DeliveryChallanService;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author Vivek.Gupta
 *
 */

@RestController
@RequestMapping("/api/v1/deliveryChallan")
@Validated
public class DeliveryChallanController {
	
	
	@Autowired
	private DeliveryChallanService deliveryChallanService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
//	@Autowired
//	private FileStorageProperties fileStorageProperties;
	
	@Value("${file.upload-dir.DcTemplate}")
    private String dcExcelPath;
	
	@Value("${file.upload-dir.DcPdfTemplate}")
    private String dcPdfPath;
	
//	 private SimpleDateFormat getSimpleDateFormat() {
//			return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//		}
//	
	
	
	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/create")
	public ResponseEntity<?> createSpareCustomerOrder(@Valid @RequestBody DeliveryChallanDetailRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareDcCreateResponse responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not created or server side error.");
			
//			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
//			errorDetails.setCount(bindingResult.getErrorCount());
//			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
//			List<String> errors = new ArrayList<>();
//			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
//			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = deliveryChallanService.createDeliveryChallan(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare delivery challan.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping(value = "/customerOrderDtl")
	public ResponseEntity<?> customerOrderDtl(@RequestParam(name = "partyBranchId") Integer partyBranchId, 
			@RequestParam(name = "productCategoryId") Integer productCategoryId, 
			@RequestParam(name = "reqType") String reqType, 
			@RequestParam(name = "branchId") BigInteger branchId,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
//		HttpHeaders headers = new HttpHeaders();
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
		reqType = reqType.equals("null") ? "Create" : reqType;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<DCcustomerOrderResponse> responseModelList = deliveryChallanService.customerOrderDtl(productCategoryId, partyBranchId, reqType,branchId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Customer Order Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Customer Order Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value="/getPartDetailsByCoNumber")
	public ResponseEntity<?> getDcPartDetail(@RequestParam(name = "customerOrderNumber") String customerOrderNumber, 
			@RequestParam() String flag,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<COpartDetailResponse> responseModelList = deliveryChallanService.getDcPartDetail(customerOrderNumber, flag, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping(value="/getPartDetailsByDcQty")
	public ResponseEntity<HeaderResponse> getPartDetailsByDcQty(@RequestBody CustomerOrderPartNoRequest resquest, 
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<DcPartQtyCalCulationResponse> responseModelList = deliveryChallanService.getPartDetailsByDcQty(resquest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getDcNumber")
	public ResponseEntity<?> searchDeliveryChallanNumber(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DeliveryChallanNumberResponse> responseModel = deliveryChallanService.searchDeliveryChallanNumber(searchText,userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + LocalDate.now());
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	


	@PostMapping(value="/search")
	public ResponseEntity<?> searchByDcFields(@RequestBody DcSearchRequest resquest, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
//		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		DcSearchListResponse responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not search or server side error.");

//			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
//			errorDetails.setCount(bindingResult.getErrorCount());
//			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
//			List<String> errors = new ArrayList<>();
//			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
//			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(CommonUtils.getErrorDetails(bindingResult));

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = deliveryChallanService.searchByDcFields(userCode, resquest,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Delivery Challan.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	 }
	
	
	@GetMapping("/cancel")
	public ResponseEntity<?> getDeliveryChallanById(@RequestParam(name = "deliveryChallanId") Integer deliveryChallanId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		DeliveryChallanHeaderAndDetailResponse responseModel = deliveryChallanService.getDeliveryChallanById(userCode, deliveryChallanId);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " );
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<?> updateStatusAndRemark(@RequestBody DcUpdateStatusRequest requestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareDcCreateResponse responseModel = deliveryChallanService.updateStatusAndRemark(userCode, requestModel);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Delivery Challan Status & Remark Updated Successfully... " );
			codeResponse.setMessage("Delivery Challan Status & Remark Updated Successfully... ");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Delivery Challan Not Update or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/exportDCReports")
	public void generateReport(@RequestBody DcSearchRequest  requestModel, @RequestParam(defaultValue = "xlsx") String format,
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
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("startDate", formatter.format(requestModel.getStartDate()));
		jasperParameter.put("endDate",formatter.format(requestModel.getEndDate()));
		jasperParameter.put("DcStatus", requestModel.getDcStatus());
		jasperParameter.put("DcNumber",requestModel.getDcNumber());
		jasperParameter.put("PartyTypeId", requestModel.getPartyTypeId());
		jasperParameter.put("ProductCategoryId", requestModel.getProductCategoryId());
		jasperParameter.put("PartyCodeId", requestModel.getPartyCodeId());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=dcExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Delivery Challan/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "SpareDeliveryChallan.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=DeliveryChallan.xlsx");
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
	
	@GetMapping("/deliverChallanPDFReport")
	public void generateGrnReport(@RequestParam(required = false) String id, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Delivery Challan Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("DChallanId", Integer.parseInt(id));
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("Flag", null);

		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=dcPdfPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Delivery Challan/pdf/";
		}
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "sparedeliverychallanfrontpage.jasper", jasperParameter,filePath);
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
	
	
	}
