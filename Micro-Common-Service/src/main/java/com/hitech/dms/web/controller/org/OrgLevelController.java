/**
 * 
 */
package com.hitech.dms.web.controller.org;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.org.OrgLevelDao;
import com.hitech.dms.web.model.org.request.OrgLeveDealerRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelByDeptRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelDealerBranchRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelHierForParentRequestModel;
import com.hitech.dms.web.model.org.response.OrgLeveDealerResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelByDeptResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelDealerBranchResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelHierForParentResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/org")
public class OrgLevelController {
	private static final Logger logger = LoggerFactory.getLogger(OrgLevelController.class);

	@Autowired
	private OrgLevelDao orgLevelDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchOrgLevelListByDept/{pcId}/{deptCode}")
	public ResponseEntity<?> fetchOrgLevelListByDept(@PathVariable Integer pcId, @PathVariable String deptCode,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelByDeptResponseModel> responseModelList = orgLevelDao.fetchOrgLevelListByDept(userCode, deptCode,
				pcId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel List By Dept Id on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List By Dept Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchOrgLevelListByDept")
	public ResponseEntity<?> fetchOrgLevelListByDept(@RequestBody OrgLevelByDeptRequestModel orgLevelByDeptRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelByDeptResponseModel> responseModelList = orgLevelDao.fetchOrgLevelListByDept(userCode,
				orgLevelByDeptRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel List By Dept Id on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List By Dept Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchOrgLevelHierForParent/{levelID}/{orgHierID}/{includeInactive}/{isFor}/{dealerId}/{departmentId}/{pcId}")
	public ResponseEntity<?> fetchOrgLevelHierForParent(@PathVariable Integer levelID,
			@PathVariable String includeInactive, @PathVariable String isFor, @PathVariable Long orgHierID,
			@PathVariable(required = false) BigInteger dealerId, @PathVariable(required = false) Integer departmentId,
			@PathVariable(required = false) Integer pcId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelHierForParentResponseModel> responseModelList = orgLevelDao.fetchOrgLevelHierForParent(userCode,
				levelID, orgHierID, includeInactive, isFor, dealerId, departmentId, pcId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel Hier For Parent on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Hier For Parent Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Hier For Parent Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchOrgLevelHierForParent")
	public ResponseEntity<?> fetchOrgLevelHierForParent(
			@RequestBody OrgLevelHierForParentRequestModel orgLevelHierForParentRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelHierForParentResponseModel> responseModelList = orgLevelDao.fetchOrgLevelHierForParent(userCode,
				orgLevelHierForParentRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel Hier For Parent on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Hier For Parent Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Hier For Parent Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchOrgLevelDealerList")
	public ResponseEntity<?> fetchOrgLevelDealerList(@RequestBody OrgLeveDealerRequestModel orgLeveDealerRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLeveDealerResponseModel> responseModelList = orgLevelDao.fetchOrgLevelDealerList(userCode,
				orgLeveDealerRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel Dealer List on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Dealer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchOrgLevelDealerBranchList")
	public ResponseEntity<?> fetchOrgLevelDealerBranchList(
			@RequestBody OrgLevelDealerBranchRequestModel orgLeveDealerBranchRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelDealerBranchResponseModel> responseModelList = orgLevelDao.fetchOrgLevelDealerBranchList(userCode,
				orgLeveDealerBranchRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel DealerBranch List on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel DealerBranch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel DealerBranch List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getPcEnquiry")
	public ResponseEntity<?> getPcEnquiry(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OrgLevelByDeptResponseModel> responseModelList = orgLevelDao.getPcEnquiry(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch OrgLevel List By Dept Id on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OrgLevel List By Dept Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
