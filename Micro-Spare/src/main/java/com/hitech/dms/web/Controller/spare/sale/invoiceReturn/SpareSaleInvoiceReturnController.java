package com.hitech.dms.web.Controller.spare.sale.invoiceReturn;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.coupon.management.CouponExcelReportRequest;
import com.hitech.dms.web.model.coupon.management.CouponReportRequest;
import com.hitech.dms.web.model.coupon.management.InvoiceExcelReportRequest;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnApproveStatus;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetail;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetailSaveResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnPdfRequest;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnRequestModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnSearchResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SearchInvoiceReturnRequest;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareInvoiceRetunSearchModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.UploadDisagreeDocumentModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.VstWithoutRefPartDtl;
import com.hitech.dms.web.service.sale.invoiceReturn.SpareSaleInvoiceReturnService;

import net.sf.jasperreports.engine.JasperPrint;

//@Author
/*
 * searchInvoiceReturnByDate
 * Rambabu 
 */

@RestController
@RequestMapping("api/v1/spareSaleInvoice/return")
public class SpareSaleInvoiceReturnController {

	@Autowired
	SpareSaleInvoiceReturnService service;
	
	
	@Value("${file.upload-dir.InvoiceReturnReport}")
	private String InvoiceReturnReport;
	

	// spare invoice return by ClaimGenerationNo

	
	@GetMapping("/fetchReturnInvoiceDetailByClaim")
	public ResponseEntity<?> fetchInvoiceDetailByGenerationNo(@RequestParam() String claimGenerationNo,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get claimGenaratioNo is " + claimGenerationNo);
		InvoiceReturnDetail response = null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();

		response = service.getInvoiceDetailByClaimGeneration(claimGenerationNo, userCode);
		System.out.println("response at controller " + response);
		if (response != null && response.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer Order Number List on " + new Date());
			codeResponse.setMessage("Customer Order Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Customer Order Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

	// api for sparesale invoice return

	@GetMapping("/fetchReturnInvoiceDetail")
	public ResponseEntity<?> fetchInvoiceDetailByGrnNo(@RequestParam() String mrnNumber,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("get GrnNo is " + mrnNumber);
		InvoiceReturnDetail response = null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		response = service.getInvoiceDetailByDOcNo(mrnNumber, userCode);
		System.out.println("response at controller " + response);
		if (response != null && response.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice fetched " + new Date());
			codeResponse.setMessage("Invoice detail fetched successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Invoice detail not found with given DocumentNo");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

	// SpareSale invoice Return Submit Detail
	@PostMapping("/SaveInvoiceReturnDetail")
	public ResponseEntity<?> InvoiceReturnDetailSave(@RequestBody InvoiceReturnRequestModel requestData,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		//System.out.println("SaveInvoice Request "+requestData)
		
		
		InvoiceReturnDetailSaveResponse response = null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();

		response = service.saveInvoiceReturnDetail(requestData, userCode);
		System.out.println("response at controller " + response);
		if (response != null && response.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage(response.getStatusMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage(response.getStatusMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	
	// spare sale invoice search data 
	@GetMapping("/spareInvoiceReturnSearch")
	public ResponseEntity<?> sapreInviceReturnSearch(
			@RequestParam() String searchType,
			@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device,HttpServletRequest request) {

		String userCode = null;
		
		
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		System.out.println("at search "+searchText +" "+searchText);
		
		List<SpareInvoiceRetunSearchModel> response = null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();

		response = service.searchInvoiceReturn(searchType, searchText, userCode);
		System.out.println("response at controller " + response);
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage("Invoice  Search Return Details ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Invoice Return search not found ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);

	}
	//InvoiceReturnRequestModel
	
	//update status after approve
	@SuppressWarnings("unchecked")
	@PostMapping("/updateInvoiceReturnStatus")
	public ResponseEntity<?> changePartyMasterStatus(
			@RequestBody  InvoiceReturnRequestModel request,
			OAuth2Authentication authentication) {
    String userCode = null;
			InvoiceReturnApproveStatus responseModel=null;
			System.out.println("the request is "+request);
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		 responseModel = service.updateInvoiceReturnStatus(request,userCode);
		if (responseModel != null 
				&& responseModel.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invooice return approve Staus ");
			codeResponse.setMessage("Invoice return Status is Updated Successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage("Invoice return Status is  not Updated  or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/*
	 * 
	 * Export Pdf report invoice Return
	 */
	@PostMapping("/InvoiceReturnExportPdf")
	 public void printCouponMgmtReport(@RequestBody InvoiceReturnPdfRequest invoiceRequest,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException
			{	
		
		
		
			OutputStream outputStream = null;
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			printStatus="true";
			System.out.println("request at controller "+invoiceRequest +" format is "+format);
			String reportName = "InvoiceReturn";
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("MRNNumber",invoiceRequest.getClaimGenerationNumber());
			jasperParameter.put("invoiceReturnNo", invoiceRequest.getInvoiceReturnNo());
			jasperParameter.put("UserCode",userCode);
			jasperParameter.put("ClaimGenerationNumber",null);
			jasperParameter.put("ReturnType",invoiceRequest.getReturnType());
			jasperParameter.put("flag",0);
			
			String filePath = "";
			String property = System.getProperty("os.name");
			if (property.contains("Windows")) {
				 filePath = InvoiceReturnReport;
			} else {
				filePath = "/var/VST-DMS-APPS/FILES/REPORTS/InvoiceReturnReport/";
			}
		try {

			JasperPrint jasperPrint = service.PdfGeneratorReportForInvoiceReturn(request, "InvoiceReturn.jasper",
					jasperParameter, filePath);
			
			  System.out.println("after jasperPrint line ");
			
			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			System.out.println("after response outputStream  ");
			
			System.out.println("before send data for"+format);

			service.printReport(jasperPrint,format,printStatus,outputStream, reportName);
			System.out.println("after printReport");


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
	
	
	
	// printExcelReport
	
	@PostMapping("/exportInvoiceExcelReport")
	public void exportENQExcelReport(@RequestBody InvoiceExcelReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		printStatus="true";
	   System.out.println("request at controller "+requestModel +"printStatus "+printStatus);
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		String reportName = "Invoice Return ";
		
		//PdfGeneratorReportForInvoiceReturn
		//printInvoiceExcelReport
		
		//SimpleDateFormat smdf = getSimpleDateFormat();
		//String fromDate = smdf.format(requestModel.getFromDate());
		//String toDate = smdf.format(requestModel.getToDate());
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("fromDate",requestModel.getFromDate()!=null?requestModel.getFromDate():null);
		jasperParameter.put("toDate", requestModel.getToDate()!=null?requestModel.getToDate():null);
		jasperParameter.put("UserCode", userCode!=null?userCode:null);
		jasperParameter.put("grnNo", requestModel.getGrnNo()!=null?requestModel.getGrnNo():null);
		jasperParameter.put("claimNo", requestModel.getClaimNo()!=null?requestModel.getClaimNo():null);
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = InvoiceReturnReport;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/InvoiceReturnReport/";
		}
		System.out.println("at excel generate ");
		JasperPrint jasperPrint = service.ExcelGeneratorReport(request, "InvoiceReturnExcel.jasper", jasperParameter,
				filePath);
		System.out.println("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				

				response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".xlsx");
				response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			}

			outputStream = response.getOutputStream();

			service.printInvoiceExcelReport(jasperPrint, format, printStatus,outputStream, reportName);

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
	

	@SuppressWarnings("unchecked")
	@PostMapping("/searchInvoiceReturnByDate")
	public ResponseEntity<?> searchInvoiceByDate(
			@RequestBody SearchInvoiceReturnRequest request,
			OAuth2Authentication authentication) {
		
		
			String userCode = null;
			InvoiceReturnSearchResponse response = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("at controller request "+request);
		response = service.searchInvoiceReturnByDate(request, userCode);
		System.out.println("after response "+response);
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice return Feteched details ");
			codeResponse.setMessage("Invoice return Details fetched successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage("Invoice return Details  not fetched successfully");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/invoiceReturnView")
	public ResponseEntity<?> invoiceReturnView(
			@RequestBody SearchInvoiceReturnRequest request,
			OAuth2Authentication authentication) {
		
		
			String userCode = null;
			InvoiceReturnSearchResponse response = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("at controller request "+request);
		response = service.invoiceReturnViewDetail(request,userCode);
		System.out.println("after response "+response);
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice return Feteched details ");
			codeResponse.setMessage("Invoice return Details fetched successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage("Invoice return Details  not fetched successfully");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	//uploadDisagreeDocument
	@SuppressWarnings("unchecked")
	@PostMapping("/uploadDisagreeDocument")
	public ResponseEntity<?> uploadDisgreeDocument(
			@ModelAttribute UploadDisagreeDocumentModel request,
			OAuth2Authentication authentication) {
			String userCode = null;
			System.out.println("the request we get is "+request);
			//List<SpareInvoiceRetunSearchModel> response = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		UploadDisagreeDocumentModel response =null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		response = service.uploadDisagreeDocument(request,userCode);
		System.out.println("after response "+response);
		if (response != null && response.getStatusCode()==200 ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Disagree Dcument saved");
			codeResponse.setMessage("Disagree Status Updated Successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage("Unable to update Disagree Document and status");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * 
	 * @param authorizationHeader
	 * @param partSearchRequestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/fetchPartNumber")
	public ResponseEntity<?> fetchPartNumberList(@RequestParam(name = "searchText") String searchText,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<partSearchResponseModel> responseModelList = service.fetchPartNumber(userCode, searchText);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + new Date());
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getInvoiceReturnPartDetails")
	public ResponseEntity<?> getInvoiceReturnPartDetails(@RequestParam(name = "partId") BigInteger partId,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		VstWithoutRefPartDtl responseModelList = service.getInvoiceReturnPartDetails(userCode, partId);
		if (responseModelList != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + new Date());
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchInvoiceReturnNum")
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
	
		List<PartyInvoiceList> responseModelList = service.fetchInvoiceReturnNumList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Invoice return number List on " + LocalDate.now());
			codeResponse.setMessage("Invoice return Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Invoice return Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	
	

}
