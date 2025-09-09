package com.hitech.dms.web.controller.registrationcertificate.create;

import java.text.SimpleDateFormat;
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
import com.hitech.dms.web.controller.pdi.create.PdiController;
import com.hitech.dms.web.controller.pdi.dto.PdiSearchDto;
import com.hitech.dms.web.controller.pdi.dto.PdiSearchResponse;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchDto;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchResponse;
import com.hitech.dms.web.dao.registrationcertificate.create.RegistrationCertificateDao;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;
import com.hitech.dms.web.model.registrationcertificate.create.response.RegistrationCreateResponseModel;
import com.hitech.dms.web.repo.dao.pdi.checklist.PdiAggregateRepo;
import com.hitech.dms.web.repo.dao.registrationcertificate.RegistrationCertificateRepo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/rc")
@SecurityRequirement(name = "hitechApis")
public class RegistrationCertificateController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationCertificateController.class);
	
	@Autowired
	private RegistrationCertificateRepo rcRepo;
	
	@Autowired
	private RegistrationCertificateDao rcDao;
	
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
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
			apiResponse.setResult(rcRepo.autoCompleteChassisNo(userCode, chassisNo));
			apiResponse.setMessage("ChassisNo get successfully.");
		}else {
			List list=List.of();
			apiResponse.setResult(list);
			apiResponse.setMessage("ChassisNo Not Available.");
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	
	@GetMapping(value = "/detailsByChassisNo")
	public ResponseEntity<?> detailsByChassisNo(@RequestParam("chassisNo") String chassisNo,
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
			apiResponse.setResult(rcRepo.detailsByChassisNo(chassisNo));
	//	}
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/updaterc")
	public ResponseEntity<?> createMachinePO( 
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart SalesMachineVinMstEntity requestModel,
			@RequestPart(required = false) List<MultipartFile> files,
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
		RegistrationCreateResponseModel responseModel = null;
		
		responseModel = rcDao.updateRegistration(userCode, requestModel,files);
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
	
	@PostMapping(value = "/rcSearch")
    public ResponseEntity<?> rcSearch(@RequestBody RegistrationSearchDto rcSearchDto,@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
        ApiResponse apiResponse = new ApiResponse();
        
        String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
        System.out.println("selectedvalue "+rcSearchDto.selectedValue);
        List<RegistrationSearchResponse> result = rcRepo.rcSearch(rcSearchDto.getChassisNo(),
        		rcSearchDto.getRegistrationNo(),
        		rcSearchDto.page,
        		rcSearchDto.size,
               userCode,
               rcSearchDto.selectedValue
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
