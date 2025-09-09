package com.hitech.dms.web.controller.installation.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.controller.installation.dto.InstallationSearchDto;
import com.hitech.dms.web.controller.installation.dto.InstallationSearchResponse;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchDto;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchResponse;
import com.hitech.dms.web.dao.installation.create.InstallationCreateDao;
import com.hitech.dms.web.entity.installation.InstallationEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.model.Installation.create.response.InstallationCreateResponseModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;
import com.hitech.dms.web.repo.dao.installation.checklist.InstallationRepo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/installation")
@SecurityRequirement(name = "hitechApis")
public class InstallationController {

	private static final Logger logger = LoggerFactory.getLogger(InstallationController.class);
	
	@Autowired
	private InstallationRepo installationRepo;
	@Autowired
	private InstallationCreateDao installationDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	
	@PostMapping("/saveInstallation")
	public ResponseEntity<?> saveInstallation( 
			@RequestPart(value="requestModel") InstallationEntity requestModel,
			//@RequestBody PdiEntity requestModel,
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			 OAuth2Authentication authentication,
			Device device, HttpServletRequest request) 
	
	
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		InstallationCreateResponseModel responseModel = null;
		
		responseModel = installationDao.createInstallation(userCode, requestModel,files);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Pdi.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/autoCompleteChassisNo")
	public ResponseEntity<?> getAutoCompleteChassisNo(

			@RequestParam("chassisNo") String chassisNo,

			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		System.out.println("chessis");
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		ApiResponse apiResponse = new ApiResponse();
		
		apiResponse.setStatus(HttpStatus.OK.value());
		if(chassisNo !=null && !chassisNo.equalsIgnoreCase("")) {
			apiResponse.setResult(installationRepo.autoCompleteChassisNo(userCode, chassisNo));
			apiResponse.setMessage("ChassisNo get successfully.");
		}else {
			List list=List.of();
			apiResponse.setResult(list);
			apiResponse.setMessage("ChassisNo Not Available.");
		}
		
		return ResponseEntity.ok(apiResponse);
	}
	
	
	@GetMapping(value = "/vinDetailsByChassisNo")
	public ResponseEntity<?> vinDetailsByChassisNo(@RequestParam("chassisNo") String chassisNo,

			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			System.out.println("chassisNo--" + chassisNo + "----");
			apiResponse.setResult(installationRepo.vinDetailsByChassisNo(chassisNo));
		return ResponseEntity.ok(apiResponse);
	}
	@GetMapping(value = "/getRepresentingList")
	public ResponseEntity<?> getRepresentingList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			apiResponse.setResult(installationRepo.getRepresentingList(userCode,"INS_REPRESENTATIVE_TYPE"));
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping(value = "/getInstallationTypeList")
	public ResponseEntity<?> getInstallationTypeList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			apiResponse.setResult(installationRepo.getInstallationTypeList(userCode,"INS_INSTALLATION_TYPE"));
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping(value = "/installationDoneBy")
	public ResponseEntity<?> installationDoneBy(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			apiResponse.setResult(installationRepo.installationDoneByList(userCode));
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping("/getAllCheckListForInstallation")
	public ResponseEntity<?> getAllCheckListForInstallation(
			@RequestParam(value = "dealerId",required = false) String dealerId,
			@RequestParam(value = "branchId",required = false) String branchId,@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		List<Map<String, Object>> code = installationRepo.getAllCheckpoint(dealerId, branchId, userCode);
		apiResponse.setMessage("Checkpoint get Successfully");
		apiResponse.setStatus(HttpStatus.OK.value());
		apiResponse.setResult(code);
		return ResponseEntity.ok(apiResponse);
	}
	
	
	@PostMapping(value = "/installationSearch")
    public ResponseEntity<?> rcSearch(@RequestBody InstallationSearchDto installatonSearchDto,@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
        ApiResponse apiResponse = new ApiResponse();
        
        String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
        
        List<InstallationSearchResponse> result = installationRepo.installationSearch(installatonSearchDto.getChassisNo(),
        		installatonSearchDto.getInstallationNo(),
        		installatonSearchDto.getFromDate(),
        		installatonSearchDto.getToDate(),
        		installatonSearchDto.getStatus(),
        		installatonSearchDto.page,
        		installatonSearchDto.size,
               userCode
               );
        apiResponse.setMessage("Registration search list");
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setResult(result);
        Long count = 0L;
        if(result!=null && result.size()>0){
        	//apiResponse.setCount(result.get(0).getRecordCount());
        	apiResponse.setCount(Long.valueOf(result.size()));
        }
        return ResponseEntity.ok(apiResponse);
    }
	
}
