package com.hitech.dms.web.controller.service.masterdata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.masterdata.search.MasterDataDao;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.LabourGroupMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.PartyMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceLabourMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceSourceEntity;
import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.model.masterdata.request.MasterDataModelRequest;
import com.hitech.dms.web.model.masterdata.request.TaxDTO;
import com.hitech.dms.web.model.masterdata.request.TaxDetailsDTO;
import com.hitech.dms.web.model.masterdata.response.MasterDataModelResponse;
import com.hitech.dms.web.model.partmaster.create.request.PartDetailsDTO;
import com.hitech.dms.web.repo.dao.masterdata.search.QutationRepo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/serviceMasterData")
@SecurityRequirement(name = "hitechApis")
public class ServiceMasterDataController {

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private MasterDataDao masterDataDao;
	
	@Autowired
	private QutationRepo quoRepo;
	
	@PostMapping("/sourceList")
	public ResponseEntity<?> searchSourceList(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceSourceEntity> responseModel = masterDataDao.searchSourceList(userCode);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Source List on " + formatter.format(new Date()));
			codeResponse.setMessage("Source List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Source List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/repairCategoryList")
	public ResponseEntity<?> searchRepairCategoryList(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<?> responseModel = masterDataDao.searchRepairCategoryList(userCode);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Repire Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/insuranceList")
	public ResponseEntity<?> insuranceList(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyMasterEntity> responseModel = masterDataDao.searchinsuranceList(userCode,"INS");
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Insurance List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Insurance List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/serviceCategoryList")
	public ResponseEntity<?> serviceCategoryList(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<?> responseModel = masterDataDao.serviceCategoryList(userCode);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/customerVoiceList")
	public ResponseEntity<?> fetchCustomerVoice(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<Integer,String> responseModel = masterDataDao.fetchCustomerVoice(userCode);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Voice List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Voice List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/chassisSearchList")
	public ResponseEntity<?> chassisSearchList(
			OAuth2Authentication authentication,@RequestBody MasterDataModelRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<MasterDataModelResponse> responseModel = masterDataDao.chassisSearchList(userCode,request);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Voice List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Voice List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getChassisDetails")
	public ResponseEntity<?> getChassisDetails(
			OAuth2Authentication authentication,@RequestParam("chassisNo") String chassisNo) {
		String userCode = null;
		System.out.println("Enter in this detail get chasis Detail"+chassisNo);
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MasterDataModelResponse responseModel = masterDataDao.getChassisDetails(userCode,chassisNo);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/getWarrantyAndAMC")
	public ResponseEntity<?> getWarrantyAndAMC(
			OAuth2Authentication authentication,@RequestBody MasterDataModelRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object>  responseModel = masterDataDao.getWarrantyAndAMC(userCode,request);
		if (responseModel != null && responseModel.size()>0
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer Voice List on " + formatter.format(new Date()));
			codeResponse.setMessage("Repire Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Voice List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	  @GetMapping("/getServiceType") public ResponseEntity<?>
	  getServiceType(OAuth2Authentication authentication,
						@RequestParam(value = "serviceCategory") Integer serviceCategory,
						@RequestParam(value = "modelFamilyId", required = false) Integer modelFamilyId) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			List<MasterDataModelResponse> serviceType = masterDataDao.getServiceType(serviceCategory, modelFamilyId);

			if (serviceType != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(serviceType);
			return ResponseEntity.ok(userAuthResponse);

		}
	 
	
		@GetMapping("/fetchLabourgroupList")
		public ResponseEntity<?> getServiceType(OAuth2Authentication authentication,

				@RequestParam(value = "vinId", required = false) Integer vinId) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			List<LabourGroupMasterEntity> tempLabourGroupList = masterDataDao.getLabouGroupDescList(vinId, userCode);

			if (tempLabourGroupList != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(tempLabourGroupList);
			return ResponseEntity.ok(userAuthResponse);

		}
		
		@PostMapping("/validateServiceType")
		public ResponseEntity<?> validateServiceType(OAuth2Authentication authentication,
				@RequestBody MasterDataModelRequest request) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			Map<String, String> resultMap = masterDataDao.validateServiceType(request, userCode);

			if (resultMap != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(resultMap);
			return ResponseEntity.ok(userAuthResponse);

		}
		
		@PostMapping("/viewVehicleDetails")
		public ResponseEntity<?> viewVehicleDetails(OAuth2Authentication authentication,
				@RequestBody MasterDataModelRequest request) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			MasterDataModelResponse resultMap = masterDataDao.getChassisDetails(userCode, request.getChassisNo());

			if (resultMap != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(resultMap);
			return ResponseEntity.ok(userAuthResponse);

		}
	
		
		@PostMapping("/fetchLabourCode")
		public ResponseEntity<?> fetchLabourCode(OAuth2Authentication authentication,
				@RequestBody MasterDataModelRequest request) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			List<ServiceLabourMasterEntity> resultMap = masterDataDao.fetchLabourCode(userCode, request);

			if (resultMap != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Labour code on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(resultMap);
			return ResponseEntity.ok(userAuthResponse);

		}
	
		@GetMapping("/fetchLbrDetails")
		public ResponseEntity<?> fetchLbrDetails(OAuth2Authentication authentication,

				@RequestParam(value = "labourCode", required = false) String labourCode,
				@RequestParam(value = "labourGroup", required = false) Integer labourGroup,
				@RequestParam(value = "chassisNo", required = false) String chassisNo,
				@RequestParam(value = "branchId", required = false) String branchId,
				@RequestParam(value="doctype",required=false) String doctype)
			{
			String userCode = null;
			//String docType="QUOLBR";
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			Map<String, Object> labour = masterDataDao.fetchLbrDetails(labourCode,labourGroup,chassisNo, userCode,branchId,doctype);

			if (labour != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(labour);
			return ResponseEntity.ok(userAuthResponse);

		}
		
		@PostMapping("/fetchTaxBreakInSingleRow")
		public ResponseEntity<?> fetchTaxBreakInSingleRow(OAuth2Authentication authentication,
				@RequestBody TaxDTO request) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			TaxDetailsDTO resultMap = masterDataDao.fetchTaxBreakInSingleRow(userCode, request);

			if (resultMap != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Labour code on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(resultMap);
			return ResponseEntity.ok(userAuthResponse);

		}
	
		@PostMapping("/searchPartNumberDetails")
		public ResponseEntity<?> searchPartNumberDetails(OAuth2Authentication authentication,
				@RequestBody MasterDataModelRequest request) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			List<AdminPartMasterEntity> partDetailsList = masterDataDao.searchPartNumberDetails(userCode, request);

			if (partDetailsList != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Labour code on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Labour code Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(partDetailsList);
			return ResponseEntity.ok(userAuthResponse);

		}
		
		@GetMapping("/getPartDetails")
		public ResponseEntity<?> getPartDetails(OAuth2Authentication authentication,
				@RequestParam(value = "branchID", required = false) Integer branchID,
				@RequestParam(value = "partNo", required = false) String partNo,
				@RequestParam(value = "docType", required = false) String docType,
				@RequestParam(value = "jobCardId", required = false) String jobCardId) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			HeaderResponse userAuthResponse = new HeaderResponse();
			MessageCodeResponse codeResponse = new MessageCodeResponse();
			SimpleDateFormat formatter = getSimpleDateFormat();
			PartDetailsDTO partDTOList = masterDataDao.getPartDetails(branchID, partNo, docType, jobCardId,userCode);

			if (partDTOList != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Successfully fetched");
			} else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Chassis Details Not Fetched or server side error.");
			}
			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(partDTOList);
			return ResponseEntity.ok(userAuthResponse);

		}
}
