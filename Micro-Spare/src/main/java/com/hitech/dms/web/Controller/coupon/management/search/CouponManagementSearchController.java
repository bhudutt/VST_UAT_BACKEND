package com.hitech.dms.web.Controller.coupon.management.search;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hitech.dms.web.model.coupon.management.ApprovalDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSaveResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchResponse;
import com.hitech.dms.web.model.coupon.management.DocumentNoModel;
import com.hitech.dms.web.model.coupon.management.UpdateCoupanRequest;
import com.hitech.dms.web.model.coupon.management.UpdateCouponResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareInvoiceRetunSearchModel;
import com.hitech.dms.web.service.coupon.management.CouponManagementService;

@RestController
@RequestMapping("coupon/search")
public class CouponManagementSearchController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(CouponManagementSearchController.class);

	@Autowired
	CouponManagementService service;
	
	
	@PostMapping("/searchCouponDetail")
	public ResponseEntity<?> searchCouponDetails(@RequestBody CouponDetailSearchRequest searchRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		System.out.println("request at controller "+searchRequest);

		CouponSearchResponse response=service.fetchCouponDetail(searchRequest, userCode);
		System.out.println("response at controller "+response);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
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
	
	
	
	
	@GetMapping("/searchDocNo")
	public ResponseEntity<?> searchDocumentNo(
			@RequestParam() String searchType,
			@RequestParam() String searchText,
			
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		System.out.println("request at controller "+searchType+" "+searchText);
		
		List<String> response=service.searchDocumentNo(searchType, searchText, userCode);
		System.out.println("response at controller "+response);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage("Data Search Successfully ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Data found Unsuccessful");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	
	// get coupon detail based on document No
	@GetMapping("/fetchCouponDetail")
	public ResponseEntity<?> fecthCouponDetailByDocNo(
			@RequestParam() String dealerCode,
			@RequestParam() String documentNo,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
			{
		
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		System.out.println("request at controller "+dealerCode+" "+documentNo);
		CouponDetailResponse response = service.getCouponDetailByDocNo(dealerCode, documentNo, userCode);
		
		System.out.println("response at controller "+response);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage("Data Search Successfully ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Data found Unsuccessful");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	// approve or update coupon amount status
	@PostMapping("/updateApproveStatus")
	public ResponseEntity<?> updateApproveStatus(@RequestBody UpdateCouponResponse updateRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		System.out.println("request at controller "+updateRequest +"docNo"+updateRequest.getDocumentNo());

		UpdateCouponResponse response=service.updateCouponApproval(updateRequest.getUpdateRequest(),
				updateRequest.getDocumentNo(),updateRequest.getApprovedAmount(),userCode);
		System.out.println("response at controller "+response);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
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
	
	
	// get Coupon detail by DateFilter 
	@SuppressWarnings("unchecked")
	@PostMapping("/getCouponDetailBydate")
	public ResponseEntity<?> couponDetailByDate(
			@RequestBody CouponSearchRequest request,
			OAuth2Authentication authentication) {
			String userCode = null;
		   CouponSearchResponse response = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	//System.out.println("at controller request "+request);
		response = service.searchCouponByDate(request,userCode);
		//System.out.println("after response "+response);
		if (response !=null) {
			{
				if(response.getStatusCode()==200) {
					
					codeResponse.setCode("EC200");
					codeResponse.setDescription("Sucessfull  ");
					codeResponse.setMessage("Coupon Detail fetched Successfully");
				}
				else {
					codeResponse.setCode("EC503");
					codeResponse.setDescription("Unsucessfull  ");
					codeResponse.setMessage("Coupon Detail not available at given Date");
				}
				
				
			}
			
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage("Invoice return Details  not fetched successfully");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	// getApproval designationLevelCode
	@SuppressWarnings("unchecked")
	@GetMapping("/getApprovalDetail")
	public ResponseEntity<?> approvalDetail(
			@RequestParam(value = "userCode", required = true) String  userCode1,
			
			OAuth2Authentication authentication) {
			String userCode = null;
			ApprovalDetailResponse response = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("at controller request userCode "+userCode1);
		response = service.getApprovalDetail(userCode);
		System.out.println("after response "+response);
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Approval Detailed Fetched ");
			codeResponse.setMessage(response.getStatusMessage());
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on ");
			codeResponse.setMessage(response.getStatusMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
}
