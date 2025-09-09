/**
 * 
 */
package com.hitech.dms.web.controller.activitygstclaim;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimGstInvSearchRequest;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvoiceRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvSearchResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvoiceResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimInvApprovalResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityCreditNoteResponseModel;
import com.hitech.dms.web.service.activity.gstclaim.ActivityGstClaimService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/activity/gst/claim")
@SecurityRequirement(name = "hitechApis")
public class ActivityGstClaimController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityGstClaimController.class);
	@Autowired

	private ActivityGstClaimService activityGstClaimService;
	
	@Value("${file.upload-dir.ActivityGstClaimInvPrint}")
	private String activityGstClaimInvPrintPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	/**
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping("/getActivityClaimList")
	public ResponseEntity<?> getActivityDetails(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityGstClaimService.getActivityClaimNoList(userCode, device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity claim  List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim Not Fetched .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authentication
	 * @param activityClaimId
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping("/getActivityClaimDetailsById")
	public ResponseEntity<?> getActivityClaimDetailsById(OAuth2Authentication authentication,
			@RequestParam(value = "activityClaimId") Integer activityClaimId, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityGstClaimService.getActivityClaimDetailsById(userCode, device,
				activityClaimId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity calim Details Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestedData
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@PostMapping("/createActivityClaimInvoice")
	public ResponseEntity<?> createActivityClaimInvoice(
			@Valid @RequestBody ActivityClaimInvoiceRequestModel requestedData, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		ActivityClaimGstInvoiceResponseModel responseModel = null;
		try {
			responseModel = activityGstClaimService.createActivityGstClaimInvoice(userCode, requestedData, device);
		
			if (responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Activity GST Claim Invoice Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Activity  GST Claim Invoice Not Created.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/fetchActivityClaimGstInvSearchList")
	public ResponseEntity<?> fetchActivityClaimGstInvSearchList(
			@RequestBody ActivityClaimGstInvSearchRequest requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityClaimGstInvSearchResponseModel> responseModelList = activityGstClaimService
				.fetchActivityClaimGstInvSearchList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Claim Invoice Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Invoice Search Not Fetched .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authentication
	 * @param activityClaimGstinvoiceId
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/getActivityGstClaimInvoiceDetailsById")
	public ResponseEntity<?> getActivityGstClaimInvoiceDetailsById(OAuth2Authentication authentication,
			@RequestParam(value = "activityClaimGstinvoiceId") Integer activityClaimGstinvoiceId, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityGstClaimService.getActivityGstClaimInvoiceDetailsById(userCode,
				device, activityClaimGstinvoiceId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim GST invoice Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity calim GST invoice  Details Not Fetched.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @return
	 */
	@PostMapping("/approveRejectActivityClaimGstInvoice")
	public ResponseEntity<?> approveRejectActivityClaimInvoice(
			@RequestBody ActivityClaimInvApprovalRequestModel requestModel, OAuth2Authentication authentication,
			Device device) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ActivityClaimInvApprovalResponseModel responseModel = activityGstClaimService
				.approveRejectActivityClaimGstInvoice(userCode, requestModel, device);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Claim Invoice Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Activity Claim Invoice Approval  Not Fetched .");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/fetchActivityCreditNoteSearchList")
	public ResponseEntity<?> fetchActivityCreditNoteSearchList(
			@RequestBody ActivityClaimGstInvSearchRequest requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ActivityCreditNoteResponseModel> responseModelList = activityGstClaimService
				.fetchActivityCreditNoteSearchList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Activity Credit Note Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Credit Note Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("ActivityCredit Note Search Not Fetched .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping("/getActivityClaimGstInvoiceList")
	public ResponseEntity<?> getActivityClaimGstInvoiceDetails(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityGstClaimService.getActivityClaimGstInvoiceList(userCode,
				device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity claim Invoice  List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim Invoice List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity claim Invoice Not Fetched .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param id
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/exportActivityGSTclaimReport")
	public void exportActivityGSTclaimReport(@RequestParam(value = "id") int id,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "ActivityClaimGstInvoice";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("ClaimGstInvoiceId", id);
		jasperParameter.put("FLAG", "TOP");

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			 filePath = activityGstClaimInvPrintPath;
			
		} else {
			
			 filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ActGstInvClaim/";
		
		}

		JasperPrint jasperPrint = activityGstClaimService.PdfGeneratorReport(request, "ActivityClaimGstServiceInvoice.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			activityGstClaimService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	
	
	
	@GetMapping("/getPlantCodeList")
	public ResponseEntity<?> getPlantCodeList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = activityGstClaimService.getPlantCodeList(userCode, device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Plant Code  List on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Plant Code Not Fetched .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
