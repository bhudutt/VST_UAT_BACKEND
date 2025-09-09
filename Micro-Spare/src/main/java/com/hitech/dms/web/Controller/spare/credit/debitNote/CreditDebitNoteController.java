package com.hitech.dms.web.Controller.spare.credit.debitNote;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spara.creditDebit.note.request.CreateCrDrNoteRequest;
import com.hitech.dms.web.model.spara.creditDebit.note.request.FilterCreditDebitNoteReq;
import com.hitech.dms.web.model.spara.creditDebit.note.response.CreditDebitNoteReponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.FilterCreditDebitNoteRep;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.ViewResponse;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderSearchRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.service.spare.creditDebit.CreditDebitNoteService;
import com.lowagie.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/creditDebit")
@Slf4j
@Validated
public class CreditDebitNoteController {
	
	
	@Autowired
	private CreditDebitNoteService creditDebitNoteService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.cdTemplate}")
    private String cdPdfPath;
	
	@Value("${file.upload-dir.cdnExcelTemplate}")
    private String dcnExcelExcelPath;
	
	
	
		
	
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
	public ResponseEntity<?> savePayment(@Valid @RequestBody CreateCrDrNoteRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)throws ParseException {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SaveResponse responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare credit debit not created or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = creditDebitNoteService.save(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage("Credit Debit Note Save Successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Payment Voucher.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getCreditAndDebitType")
	public ResponseEntity<?> getCreditAndDebitType(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "CR_DR_VOU_TYPE";
		List<PaymentVoucherList> responseList = creditDebitNoteService.getCreditAndDebitType(lookupTypeCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Credit Debit Voucher Type List on " + LocalDate.now());
			codeResponse.setMessage("Payment Voucher List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Credit Debit Voucher Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/partyTypeList")
	public ResponseEntity<?> partyTypeList(@RequestParam(name = "dealerTypeId") Integer dealerTypeId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "CR_DR_VOU_TYPE";
		List<PartyCategoryResponse> responseList = creditDebitNoteService.partyTypeList(dealerTypeId);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Credit Debit Voucher Type List on " + LocalDate.now());
			codeResponse.setMessage("Payment Voucher List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Credit Debit Voucher Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/getCreditDebitNumber")
	public ResponseEntity<?> searchCreditDebitNumber(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<SaveResponse> responseModel = creditDebitNoteService.searchCreditDebitNumber(searchText,userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " +LocalDate.now());
			codeResponse.setMessage("Credit Debit No. Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " +LocalDate.now());
			codeResponse.setMessage("Credit Debit No. Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("filterCreditDebitNote")
	public ResponseEntity<?> filterCreditDebitNote(@Valid @RequestBody FilterCreditDebitNoteReq requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request){
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		FilterCreditDebitNoteRep responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare credit debit not fetch or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = creditDebitNoteService.filter(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage("fatch data successfully...");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Payment Voucher.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view")
	public ResponseEntity<?> viewCreditDebitDetail(@RequestParam(name = "creditDebitNoteId") BigInteger creditDebitNoteId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		ViewResponse responseModel = creditDebitNoteService.viewCreditDebitDetail(creditDebitNoteId,userCode);
		System.out.println("responseModel Date "+responseModel.getCreditDebitDate());
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successfully" +LocalDate.now());
			codeResponse.setMessage("View Credit Debit Details");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " +LocalDate.now());
			codeResponse.setMessage("View Credit Debit Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
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

	
	
	@PostMapping("/creditDebitNoteExcelReport")
	public void generateReport(@RequestBody FilterCreditDebitNoteReq requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
//		String reportName="Credit Debit Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
					
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("creditDebitNo", requestModel.getCreditDebitNo());
		jasperParameter.put("startDate", formatter.format(requestModel.getFromDate()));
		jasperParameter.put("toDate", formatter.format(requestModel.getToDate()));
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=dcnExcelExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Credit-Debit-Note/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "CreditNoteExcel.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=CreditNoteExcel.xlsx");
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
	

	
	@GetMapping("/creditDebitNotePDFReport")
	public void generateGrnReport(@RequestParam(name = "creditDebitNoteId") BigInteger creditDebitNoteId, 
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Credit Debit Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("creditDebitNoteId", creditDebitNoteId.intValue());
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("flag", 1);

		
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=cdPdfPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Credit-Debit-Note/pdf/";
		}
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "CreditNoteFront.jasper", jasperParameter,filePath);
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

}
