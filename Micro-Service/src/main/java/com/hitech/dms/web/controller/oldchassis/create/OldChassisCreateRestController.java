package com.hitech.dms.web.controller.oldchassis.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.oldchassis.OldChassisCreateDao;
import com.hitech.dms.web.model.oldchassis.OldChassisCreateRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCreateResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisItemDescriptionRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByChassisRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByChassisResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByVinIDRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByVinIDResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisModelItemListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisModelListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSegmentListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSeriesListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisVariantListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldchassisItemDescriptionResponseModel;
import com.hitech.dms.web.model.oldchassis.PoPlantRequstModel;
import com.hitech.dms.web.model.oldchassis.PoPlantResponseModel;
import com.hitech.dms.web.model.oldchassis.list.response.OldchassisListResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisEngVinRegDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisMachineItemIdDetailModel;
import com.hitech.dms.web.model.oldchassis.request.OldChassisEngVinRegDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/oldChassis")
@SecurityRequirement(name = "hitechApis")
public class OldChassisCreateRestController {

private static final Logger logger = LoggerFactory.getLogger(OldChassisCreateRestController.class);
	
	@Autowired
	private OldChassisCreateDao oldChassisCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createOldChassis")
	public ResponseEntity<?> createOldChassis(@RequestBody OldChassisCreateRequestModel requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		OldChassisCreateResponseModel responseModel = oldChassisCreateDao.createOldChassis(userCode,requestModel,device);
		if(responseModel.getStatusCode() == 201) {
		try {
			oldChassisCreateDao.oldChassisMail(userCode, "Old Chassis Approval", requestModel.getChassisMachineVinMstEntity().getVinId(),requestModel.getChassisMachineVinMstEntity().getStatus()).subscribe(e -> {
				logger.info(e.toString());
			});
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		}
		}
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Old Chassis added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis Not Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	@GetMapping("/fetchCustDTLByMobileNo/{mobileNumber}/{isFor}/{dealerID}")
	public ResponseEntity<?> fetchCustDTLByMobileNo(@PathVariable String mobileNumber, @PathVariable String isFor,
			@PathVariable Long dealerID, OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisCustomerDTLByMobileResponseModel> responseModelList = oldChassisCreateDao.fetchCustomerDTLByMobileNo(userCode,
				mobileNumber, isFor, dealerID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Mobile No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
		

	@PostMapping("/fetchCustomerDTLByMobileNo")
	public ResponseEntity<?> fetchCustomerDTLByMobileNo(
			@RequestBody OldChassisCustomerDTLByMobileRequestModel customerDTLByMobileRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			customerDTLByMobileRequestModel.setUserCode(userCode);
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisCustomerDTLByMobileResponseModel> responseModelList = oldChassisCreateDao
				.fetchCustomerDTLByMobileNo(customerDTLByMobileRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Mobile No. on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Mobile No. Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchCustomerDTLByCustID/{customerID}")
	public ResponseEntity<?> fetchCustomerDTLByCustID(@PathVariable Long customerID,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisCustomerDTLByCustIDResponseModel> responseModelList = oldChassisCreateDao.fetchCustomerDTLByCustID(userCode,
				customerID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchCustomerDTLByCustID")
	public ResponseEntity<?> fetchCustomerDTLByCustID(
			@RequestBody OldChassisCustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisCustomerDTLByCustIDResponseModel> responseModelList = oldChassisCreateDao.fetchCustomerDTLByCustID(userCode,
				customerDTLByCustIDRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Detail By Customer Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Detail By Customer Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchSeriesList")
	public ResponseEntity<?> fetchSeriesList(@RequestParam("pcId") Integer pcId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisSeriesListResponseModel> responseModelList = oldChassisCreateDao.fetchSeriesList(userCode,device, pcId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Series List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Series List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Series List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchSegmentList")
	public ResponseEntity<?> fetchSegmentList(@RequestParam("modelId") BigInteger modelId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisSegmentListResponseModel> responseModelList = oldChassisCreateDao.fetchSegmentList(userCode,device, modelId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Segment List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Segment List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Segment List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchMachineItemByList")
	public ResponseEntity<?> fetchModelList(@RequestParam("machineItemId") Integer machineItemId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisModelListResponseModel> responseModelList = oldChassisCreateDao.fetchMachineItemByList(userCode,device,machineItemId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchVariantList")
	public ResponseEntity<?> fetchVariantList(@RequestParam("modelId") BigInteger modelId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisVariantListResponseModel> responseModelList = oldChassisCreateDao.fetchVariantList(userCode,device,modelId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Variant List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Variant List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Variant List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchModelAllList")
	public ResponseEntity<?> fetchModelAllList(
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingModelDTLResponseModel> responseModelList = oldChassisCreateDao.fetchModelAllList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Model Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Model Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchModelDetailsAllList")
	public ResponseEntity<?> fetchModelDetailsAllList(
			@RequestParam("machineItemId") BigInteger machineItemId,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisMachineItemIdDetailModel> responseModelList = oldChassisCreateDao.fetchModelDetailsAllList(userCode,device,machineItemId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Details List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchOldChassisItemDTLList")
	public ResponseEntity<?> fetchOldChassisItemDTLList(@RequestBody OldChassisItemDescriptionRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldchassisItemDescriptionResponseModel> responseModelList = oldChassisCreateDao.fetchOldChassisItemDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchOldChassisDTLList")
	public ResponseEntity<?> fetchOldChassisDTLList(@RequestBody OldChassisNumberRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisNumberResponseModel> responseModelList = oldChassisCreateDao.fetchOldChassisDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Chassis List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Chassis List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	//OldChassisFetchDTLResponseModel

	@PostMapping("/fetchOldChassisMultiDTLList")
	public ResponseEntity<?> fetchOldChassisMultiDTLList(@RequestBody OldChassisFetchDTLRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisFetchDTLResponseModel> responseModelList = oldChassisCreateDao.fetchOldChassisMultiDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Chassis List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Chassis List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchOldchassisList")
	public ResponseEntity<?> fetchOldchassisList(
			 OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldchassisListResponseModel> responseModelList = oldChassisCreateDao.fetchOldchassisList(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Old Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	@PostMapping("/fetchOldChassisEnginVinRegistrationDTLList")
	public ResponseEntity<?> fetchOldChassisEnginVinRegistrationDTLList(@RequestBody OldChassisEngVinRegDTLRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OldChassisEngVinRegDTLResponseModel> responseModelList = oldChassisCreateDao.fetchOldChassisEnginVinRegistrationDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Details List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchPOPlantList")
	public ResponseEntity<?> fetchPOPlantList(@RequestBody PoPlantRequstModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PoPlantResponseModel> responseModelList = oldChassisCreateDao.fetchPOPlantList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PO Plant List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine PO Plant List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine PO Plant List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
}
