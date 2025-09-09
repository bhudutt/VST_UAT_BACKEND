package com.hitech.dms.web.controller.competitor.master;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.hitech.dms.web.dao.competitor.master.CompetitorMasterDao;
import com.hitech.dms.web.entity.competitor.master.CompetitorMasterEntity;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterBrandListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/enquiry")
@SecurityRequirement(name = "hitechApis")
public class CompetitorMasterCreateRestController {	
private static final Logger logger = LoggerFactory.getLogger(CompetitorMasterCreateRestController.class);
	
    private Map<String, Object> map = new HashMap<>();
	@Autowired
	private  CompetitorMasterDao competitorMasterDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createCompetitorMaster")
	public ResponseEntity<?> createCompetitorMaster(@RequestBody CompetitorMasterEntity competitorMasterEntity,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CompetitorMasterResponseModel responseModel = competitorMasterDao.createCompetitorMaster(userCode, competitorMasterEntity,
				 device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Competitor Master added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Competitor Master Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	@GetMapping("/fetchCompetitorMasterList")
	public ResponseEntity<?> fetchCompetitorMasterList(
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<CompetitorMasterListResponseModel> responseModelList = competitorMasterDao.fetchCompetitorMasterList(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Competitor Master List on " + formatter.format(new Date()));
			codeResponse.setMessage("Competitor Master List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Competitor Master List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchBrandList")
	public ResponseEntity<?> fetchfetchBrandList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<CompetitorMasterBrandListResponseModel> competitormasterList = competitorMasterDao.fetchfetchBrandList(userCode, null);
		if (competitormasterList!=null) {
			codeResponse.setDescription("Brand List List on" + formatter.format(new Date()));
			codeResponse.setMessage("Brand List Successfully");
		}else {
			codeResponse.setDescription("Brand List  Data not available" + formatter.format(new Date()));
			codeResponse.setMessage("Brand List fetch unsuccessfully");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(competitormasterList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/changeActiveStatus")
    public ResponseEntity<?> changeActiveStatus(@RequestParam ("id") Integer id,@RequestParam ("isActive") Character isActive,
    		OAuth2Authentication authentication)
	   {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		CompetitorMasterResponseModel responseModel = competitorMasterDao.changeActiveStatus(userCode,id,isActive);
				
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Change Active Status Updated on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Change Active Status or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	
	}
  
	

}
