package com.hitech.dms.web.Controller.spare.paymentVoucher;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSearchRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.FilterPaymentVoucherReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.PVpartyCodeRequestModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.PartyTypeReqest;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocForGrnInvReq;
import com.hitech.dms.web.model.spara.payment.voucher.response.AdvAmtResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.GrnInvReferenceDocList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentBankList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spara.payment.voucher.response.SearchPaymentVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.viewPayVoucherResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.service.spare.paymentVoucher.PaymentVoucherService;
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
@RequestMapping("/api/v1/PaymentVoucher")
@Slf4j
@Validated
public class PaymentVoucherController {
	
	@Autowired
	private PaymentVoucherService paymentVoucherService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.pvExcelTemplate:C:\\VST-DMS-APPS\\REPORTS\\Payment-Voucher\\Excel\\\\}")
    private String pvExcelPath;
	
	@Value("${file.upload-dir.pvTemplate}")
    private String dcPdfPath;
	
		
	
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
	public ResponseEntity<?> savePayment(@Valid @RequestBody CreatePayVoucherRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		PayVoucherResponse responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare payment voucher not created or server side error.");

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
		responseModel = paymentVoucherService.savePayment(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Payment Voucher.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchVoucherType")
	public ResponseEntity<?> fetchVoucherType(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "PAYMENT_VOUCHER";
		List<PaymentVoucherList> responseList = paymentVoucherService.fetchPaymentReceiptList(lookupTypeCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Payment Voucher List on " + LocalDate.now());
			codeResponse.setMessage("Payment Voucher List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Payment Voucher List List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchRefDoc")
	public ResponseEntity<?> fetchRefDoc(OAuth2Authentication authentication, Device device,@RequestParam(name="pvTypeCode") String pvTypeCode,
			@RequestParam(name="partyCode") String partyCode,	HttpServletRequest request) {

		String[] partyCodeSplit = partyCode.split("\\|");
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		List<String> paymentTypeDoc = Arrays.asList("SPARES GRN","APR RETURN","ADVANCE");
		List<String> receiptTypeDoc = Arrays.asList("SPARES SALE INVOICE","ADVANCE");
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "REFERENCE_DOC";
		AdvAmtResponse finalResponse = new AdvAmtResponse();
		List<PaymentVoucherList> resList = new ArrayList<>();
		List<PaymentVoucherList> responseList = paymentVoucherService.fetchPaymentReceiptList(lookupTypeCode);
		BigDecimal advAmt = paymentVoucherService.getPartyWiseAmt(partyCodeSplit[0].trim(),pvTypeCode);				
		if(pvTypeCode.equals("PAYMENT")) {
			for(PaymentVoucherList list:responseList) {
				if(paymentTypeDoc.contains(list.getValueCode())) {
					resList.add(list);
				}
			}
		}if(pvTypeCode.equals("RECEIPT")) {
			for(PaymentVoucherList list:responseList) {
				if(receiptTypeDoc.contains(list.getValueCode())) {
					resList.add(list);
				}
			}
		}
		if (!resList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Refernce Doc List on " + LocalDate.now());
			codeResponse.setMessage("Refernce Doc List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Refernce Doc List Not Fetched or server side error.");
		}
		finalResponse.setAdvAmt(advAmt);
		finalResponse.setRefDocList(resList);
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(finalResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchReceiptMode")
	public ResponseEntity<?> fetchReceiptMode(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "RECEPIT_MODE";
		List<PaymentVoucherList> responseList = paymentVoucherService.fetchPaymentReceiptList(lookupTypeCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Refernce Doc List on " + LocalDate.now());
			codeResponse.setMessage("Receipt Mode List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Receipt Mode List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchCardType")
	public ResponseEntity<?> fetchCardType(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String lookupTypeCode = "CARD_TYPE";
		List<PaymentVoucherList> responseList = paymentVoucherService.fetchPaymentReceiptList(lookupTypeCode);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Refernce Doc List on " + LocalDate.now());
			codeResponse.setMessage("Receipt Mode List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Receipt Mode List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/partyTypeCategory")
	public ResponseEntity<?> partyTypeCategory(@RequestBody PartyTypeReqest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<PartyCategoryResponse> responseModel = paymentVoucherService.searchPartyTypeCategory(requestModel,userCode);
		
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
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/getBankCategory")
	public ResponseEntity<?> getBankCategory(@RequestParam(name = "bankCode") String bankCode,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<PaymentBankList> responseModel = paymentVoucherService.getBankCategory(bankCode,userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bank List on " + LocalDate.now());
			codeResponse.setMessage("Bank List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Bank List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/search")
	public ResponseEntity<?> searchAllotList(@RequestBody PVpartyCodeRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		PartyCodeListResponseModel responseModel = paymentVoucherService.searchPartyCodeList(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party code Search List on " + LocalDate.now());
			codeResponse.setMessage("Party Code Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Party Code Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
//	@PostMapping("/getPartyCodeByPartyType")
//	public ResponseEntity<?> searchAllotList(@RequestBody PVpartyCodeRequestModel requestModel,
//			OAuth2Authentication authentication) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		PartyCodeListResponseModel responseModel = paymentVoucherService.searchPartyCodeList(userCode, requestModel);
//		if (responseModel != null) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch party code Search List on " + LocalDate.now());
//			codeResponse.setMessage("Party Code Search List Successfully fetched");
//		} else {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
//			codeResponse.setMessage("Party Code Search List Not Fetched or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModel);
//		return ResponseEntity.ok(userAuthResponse);
//	}
	
	
	@PostMapping("/getRefDocDetail")
	public ResponseEntity<?> getGrnDetail(@RequestBody RefDocForGrnInvReq requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		GrnInvReferenceDocList responseModel = paymentVoucherService.getRefDocDetail(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Get GRN detail List on " + LocalDate.now());
			codeResponse.setMessage("Get GRN detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Get GRN detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("searchPaymentVoucher")
	public ResponseEntity<?> filterCreditDebitNote(@Valid @RequestBody FilterPaymentVoucherReq requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request){
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SearchPaymentVoucherResponse responseModel = null;

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
		responseModel = paymentVoucherService.filter(authorizationHeader, userCode, requestModel,device);
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
	@GetMapping("/searchPMTVouNumber")
	public ResponseEntity<?> searchPaymentVoucherNumber(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<SaveResponse> responseModel = paymentVoucherService.searchPaymentVoucherNumber(searchText,userCode);
		
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
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view")
	public ResponseEntity<?> viewPaymentVoucherDetail(@RequestParam(name = "paymentVoucherId") BigInteger paymentVoucherId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		viewPayVoucherResponse responseModel = paymentVoucherService.viewPaymentVoucherDetail(paymentVoucherId,userCode);
		
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
	
	@SuppressWarnings("unchecked")
	@GetMapping("/partyCodeList")
	public ResponseEntity<?> partyCodeList(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		List<PartyCategoryResponse> responseModel = paymentVoucherService.partyCodeList(searchText, userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + new Date());
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/getPartyNameList")
	public ResponseEntity<?> getPartyNameList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<SparePoDealerAndDistributerSearchResponse> responseModelList = paymentVoucherService.getPartyNameList(userCode, sparePoDealerAndDistributorRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("dealer and distributor mapping List on " +  LocalDate.now());
			codeResponse.setMessage("dealer and distributor mapping Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("dealer and distributor mapping Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/getPartyNameDetails")
	public ResponseEntity<?> getPartyNameDetails(@RequestParam() Integer distributorId,
			@RequestParam(required = false) BigInteger headerId, @RequestParam(required = false) Integer parentDealerId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		DistributorDetailResponse distributorDetailResponse = null;
	
			distributorDetailResponse = paymentVoucherService.getPartyNameDetails(distributorId);
		
		if (distributorDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + LocalDate.now());
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(distributorDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	@GetMapping("/getPartyWisePVDetails")
	public ResponseEntity<?> getPartyWisePVDetails(@RequestParam() Integer distributorId,@RequestParam() String flag,
			@RequestParam(required = false) BigInteger headerId, @RequestParam(required = false) Integer parentDealerId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		DistributorDetailResponse distributorDetailResponse = null;
	
			distributorDetailResponse = paymentVoucherService.getPartyWisePVDetails(distributorId,flag);
		
		if (distributorDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + LocalDate.now());
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(distributorDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	@SuppressWarnings("unchecked")
	@GetMapping("/getPvCategory")
	public ResponseEntity<?> getPvCategory(OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		
		List<PartyCategoryResponse> responseModel = paymentVoucherService.getPvCategory(userCode).stream().filter(s -> s.getPartyCategoryName()!=("FINANCIER") || s.getPartyCategoryName()!=("INSURER")).collect(Collectors.toList());
		
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
	
	

	@PostMapping("/paymentVoucherExcelReport")
	public void generateReport(@RequestBody FilterPaymentVoucherReq requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("paymentVoucherNo", requestModel.getPaymentVoucherNo());
		jasperParameter.put("voucherType", requestModel.getVoucherTypeId());
		jasperParameter.put("partyTypeId", requestModel.getPartyTypeId());
		jasperParameter.put("partyCodeId", requestModel.getPartyCodeId());
		jasperParameter.put("startDate", formatter.format(requestModel.getFromDate()));
		jasperParameter.put("toDate", formatter.format(requestModel.getToDate()));
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
	       
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=pvExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/payment-voucher/Excel";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "PaymentVoucherExcel.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=PaymentVoucherExcel.xlsx");
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
	
	@GetMapping("/paymentVoucherPDFReport")
	public void generateGrnReport(@RequestParam(required = false) String id, @RequestParam(required = false) String voucherType,
			@RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Payment Voucher Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("paymentVoucherId", Integer.parseInt(id));
		jasperParameter.put("FLAG", 0);
		jasperParameter.put("PaymentMode", voucherType);
//		jasperParameter.put("Usercode", userCodes);

		JasperPrint jasperPrint =null;
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=dcPdfPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Payment-Voucher/pdf/";
		}
		if(voucherType.equalsIgnoreCase("payment")){
			 jasperPrint = xlsxGeneratorReport(request, "PaymentVoucher.jasper", jasperParameter,filePath);
		}else {
			 jasperPrint = xlsxGeneratorReport(request, "ReceiptVoucher.jasper", jasperParameter,filePath);
		}
		
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
