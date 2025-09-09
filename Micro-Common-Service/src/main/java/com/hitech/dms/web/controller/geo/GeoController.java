/**
 * 
 */
package com.hitech.dms.web.controller.geo;

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
import com.hitech.dms.web.dao.geo.GeoDTLDao;
import com.hitech.dms.web.model.geo.request.GeoCityDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoDistricDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoPinDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoStateDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoTehsilDTLRequestModel;
import com.hitech.dms.web.model.geo.response.GeoCityDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoCountryDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoDistricDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoPinDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoTehsilDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/geo")
public class GeoController {
	private static final Logger logger = LoggerFactory.getLogger(GeoController.class);

	@Autowired
	private GeoDTLDao geoDTLDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchDistrictListByDealerID/{dealerID}/{stateId}")
	public ResponseEntity<?> fetchDistrictListByDealerID(@PathVariable Long dealerID,
			@PathVariable(required = false) BigInteger stateId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoDistricDTLResponseModel> responseModelList = geoDTLDao.fetchDistrictListByDealerID(userCode, dealerID,
				stateId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch District List By Dealer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchDistrictListByDealerID")
	public ResponseEntity<?> fetchDistrictListByDealerID(
			@RequestBody GeoDistricDTLRequestModel geoDistricDTLRequestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoDistricDTLResponseModel> responseModelList = geoDTLDao.fetchDistrictListByDealerID(userCode,
				geoDistricDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch District List By Dealer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchCityListByTehsilID/{tehsilID}")
	public ResponseEntity<?> fetchCityListByTehsilID(@PathVariable Long tehsilID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoCityDTLResponseModel> responseModelList = geoDTLDao.fetchCityListByTehsilID(userCode, tehsilID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch City List By District Id on " + formatter.format(new Date()));
			codeResponse.setMessage("City List By District Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("City List By District Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	@PostMapping("/fetchCityListByTehsilID")
	public ResponseEntity<?> fetchCityListByTehsilID(@RequestBody GeoCityDTLRequestModel geoDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoCityDTLResponseModel> responseModelList = geoDTLDao.fetchCityListByTehsilID(userCode,
				geoDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch City List By District Id on " + formatter.format(new Date()));
			codeResponse.setMessage("City List By District Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("City List By District Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPinListByCityID/{cityID}")
	public ResponseEntity<?> fetchPinListByCityID(@PathVariable Long cityID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoPinDTLResponseModel> responseModelList = geoDTLDao.fetchPinListByCityID(userCode, cityID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Pin List By City Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPinListByCityID")
	public ResponseEntity<?> fetchPinListByCityID(@RequestBody GeoPinDTLRequestModel geoPinDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoPinDTLResponseModel> responseModelList = geoDTLDao.fetchPinListByCityID(userCode,
				geoPinDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Pin List By City Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pin List By City Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchCountryList")
	public ResponseEntity<?> fetchCountryList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoCountryDTLResponseModel> responseModelList = geoDTLDao.fetchCountryList(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Country List on " + formatter.format(new Date()));
			codeResponse.setMessage("Country List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Country List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchGeoDTL/{pinID}")
	public ResponseEntity<?> fetchGeoDTL(@PathVariable Long pinID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoDTLResponseModel> responseModelList = geoDTLDao.fetchGeoDTL(userCode, pinID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Geo List By Pin Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Geo List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Geo List By Pin Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchGeoDTL")
	public ResponseEntity<?> fetchGeoDTL(@RequestBody GeoDTLRequestModel geoDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoDTLResponseModel> responseModelList = geoDTLDao.fetchGeoDTL(userCode, geoDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Geo List By Pin Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Geo List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Geo List By Pin Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchStateListByCountryID/{countryID}/{isFor}")
	public ResponseEntity<?> fetchStateListByCountryID(@PathVariable Long countryID,
			@PathVariable(required = false) String isFor,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoStateDTLResponseModel> responseModelList = geoDTLDao.fetchStateListByCountryID(userCode, countryID, isFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State List By Country Id on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State List By Country Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchStateDtlByStateID/{stateId}")
	public ResponseEntity<?> fetchStateDtlByStateID(@PathVariable Long stateId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		GeoStateDTLResponseModel responseModel = geoDTLDao.fetchStateDtlByStateID(userCode, stateId);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State Detail By State Id on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State Detail By State Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchStateDtlByStateID")
	public ResponseEntity<?> fetchStateDtlByStateID(@RequestBody GeoStateDTLRequestModel geoStateDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		GeoStateDTLResponseModel responseModel = geoDTLDao.fetchStateDtlByStateID(userCode, geoStateDTLRequestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State Detail By State Id on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State Detail By State Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchStateListByCountryID")
	public ResponseEntity<?> fetchStateListByCountryID(@RequestBody GeoStateDTLRequestModel geoStateDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoStateDTLResponseModel> responseModelList = geoDTLDao.fetchStateListByCountryID(userCode,
				geoStateDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch State List By Country Id on " + formatter.format(new Date()));
			codeResponse.setMessage("State List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State List By Country Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchTehsilListByDistrictID/{districtID}/{dealerID}/{isFor}")
	public ResponseEntity<?> fetchTehsilListByDistrictID(@PathVariable(required = false) Long districtID,
			@PathVariable(required = false) Long dealerID, @PathVariable(required = false) String isFor,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoTehsilDTLResponseModel> responseModelList = geoDTLDao.fetchTehsilListByDistrictID(userCode, districtID,
				dealerID, isFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Tehsil List By District Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List By District Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	// Get New Tehsil API
	@PostMapping("/fetchListTehsilListByDistrictID")
	public ResponseEntity<?> fetchListTehsilListByDistrictID(@RequestBody GeoTehsilDTLRequestModel geoTehsilDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoTehsilDTLResponseModel> responseModelList = geoDTLDao.fetchListTehsilListByDistrictID(userCode,
				geoTehsilDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Tehsil List By District Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List By District Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	//end
	
	
	@PostMapping("/fetchTehsilListByDistrictID")
	public ResponseEntity<?> fetchTehsilListByDistrictID(@RequestBody GeoTehsilDTLRequestModel geoTehsilDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoTehsilDTLResponseModel> responseModelList = geoDTLDao.fetchTehsilListByDistrictID(userCode,
				geoTehsilDTLRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Tehsil List By District Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Tehsil List By District Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	// Get list API
	@GetMapping("/fetchListDistrictListByDealerID")
	public ResponseEntity<?> fetchListDistrictListByDealerID(
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<GeoDistricDTLResponseModel> responseModelList = geoDTLDao.fetchListDistrictListByDealerID(userCode,null);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch District List By Dealer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("District List By Dealer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
