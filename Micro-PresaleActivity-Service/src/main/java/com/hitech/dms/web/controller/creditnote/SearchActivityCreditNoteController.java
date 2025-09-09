package com.hitech.dms.web.controller.creditnote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.web.dao.activity.creditnote.ActivitySearchCreditNoteDao;
import com.hitech.dms.web.model.activity.create.response.PoPlantResponseModel;
import com.hitech.dms.web.model.activitycreditnote.search.request.SearchActivityCreditNoteRequestModel;
import com.hitech.dms.web.model.activitycreditnote.search.response.SearchActivityCreditNoteResultResponseModel;
import com.hitech.dms.web.service.activityFiled.location.GetActivityFieldLocationList;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/activity")
@SecurityRequirement(name = "hitechApis")
public class SearchActivityCreditNoteController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchActivityCreditNoteController.class);

	@Autowired
	private ActivitySearchCreditNoteDao activitySearchCreditNoteDao;
	
	@Autowired
	private GetActivityFieldLocationList serviceActivity;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@GetMapping("/getFieldActivityLocation")
	public ResponseEntity<?> fecthCouponDetailByDocNo(@RequestParam(name = "dealerId") Integer dealerId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
			{
		
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		List<PoPlantResponseModel> LocationList= serviceActivity.getAllLocationList(userCode,dealerId);
		System.out.println("request at controller serviceActivity ");
		
		System.out.println("response at controller "+LocationList);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (LocationList != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage("Data Search Successfully ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Data found Unsuccessful");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(LocationList);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	
	@PostMapping(value = "/searchActivityCreditNoteList")
	public ResponseEntity<?> searchlistActivityCreditNoteList(@RequestBody SearchActivityCreditNoteRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		SearchActivityCreditNoteResultResponseModel responseModel = activitySearchCreditNoteDao.searchlistActivityCreditNoteList(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearchResult().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Activity Claim Credit Note Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Credit Note Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Activity Claim Credit Note Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
}
