/**
 * 
 */
package com.hitech.dms.web.controller.pc;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.pc.ProfitCenterDao;
import com.hitech.dms.web.model.pc.response.ProfitCenterUnderUserResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/pcUnderUser")
public class ProfitCenterController {
	private static final Logger logger = LoggerFactory.getLogger(ProfitCenterController.class);

	@Autowired
	private ProfitCenterDao profitCenterDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchPcUnderUserList")
	public ResponseEntity<?> fetchPcUnderUserList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ProfitCenterUnderUserResponseModel> responseModelList = profitCenterDao.fetchPcUnderUserList(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PC List Under User on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List Under User List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List Under User Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
