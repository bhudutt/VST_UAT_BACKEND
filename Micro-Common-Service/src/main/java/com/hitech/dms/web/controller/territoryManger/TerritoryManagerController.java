package com.hitech.dms.web.controller.territoryManger;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;

import com.hitech.dms.web.dao.territoryManager.TerritoryManagerDao;
import com.hitech.dms.web.model.territoryManager.TerritoryManagerListModel;

@Validated
@RestController
@RequestMapping("/territoryManager")
public class TerritoryManagerController {
	private static final Logger logger = LoggerFactory.getLogger(TerritoryManagerController.class);
	@Autowired
	private TerritoryManagerDao territoryManagerDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	
	@GetMapping("/list/{territoryId}")
	public ResponseEntity<?> fetchSalesmanList(@PathVariable int territoryId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<TerritoryManagerListModel> responseModelList = territoryManagerDao.fetchTerritoryManagerList(userCode, territoryId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Salesman List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
