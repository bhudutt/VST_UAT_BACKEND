package com.hitech.dms.web.controller.pdi.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.validation.BindingResult;
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
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.controller.pdi.dto.PdiSearchDto;
import com.hitech.dms.web.controller.pdi.dto.PdiSearchResponse;
import com.hitech.dms.web.dao.pdi.create.PdiCreateDao;
import com.hitech.dms.web.entity.pdi.OutwardPdiEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;
import com.hitech.dms.web.repo.dao.pdi.checklist.OutwardPdiAggregateRepo;
import com.hitech.dms.web.repo.dao.pdi.checklist.OutwardPdiRepository;
import com.hitech.dms.web.repo.dao.pdi.checklist.PdiAggregateRepo;
import com.hitech.dms.web.repo.dao.pdi.checklist.PdiRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/outwardpdi")
@SecurityRequirement(name = "hitechApis")
public class OutwardPdiController {

	private static final Logger logger = LoggerFactory.getLogger(OutwardPdiController.class);

	@Autowired
	private OutwardPdiAggregateRepo pdiAggregateRepo;
	
	/*
	 * @Autowired private ServicePdiImpl servicePdiImpl;
	 */
	 @Autowired 
	private PdiCreateDao pdiDao;
	 
	 @Autowired
	 private OutwardPdiRepository pdiRepository;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	/*
	 * @PostMapping(value = "/saveOutwardPdi") public ResponseEntity<?>
	 * createMachinePO(
	 * 
	 * @RequestHeader(value = "Authorization", required = true) String
	 * authorizationHeader,
	 * 
	 * @RequestBody OutwardPdiEntity requestModel,
	 * 
	 * @RequestPart(required = false) List<MultipartFile> files, BindingResult
	 * bindingResult, OAuth2Authentication authentication, Device device,
	 * HttpServletRequest request) {
	 */
	
	@PostMapping("/saveOutwardPdi")
	public ResponseEntity<?> createMachinePO( 
			@RequestPart(value="requestModel") OutwardPdiEntity requestModel,
			//@RequestBody PdiEntity requestModel,
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			 OAuth2Authentication authentication,
			Device device, HttpServletRequest request)  {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PdiCreateResponseModel responseModel = null;
		
		responseModel = pdiDao.createOutwardPdi( userCode, requestModel,files);
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
			apiResponse.setResult(pdiAggregateRepo.autoCompleteChassisNo(userCode, chassisNo));
			apiResponse.setMessage("ChassisNo get successfully.");
		}else {
			List list=List.of();
			apiResponse.setResult(list);
			apiResponse.setMessage("ChassisNo Not Available.");
		}
		
		return ResponseEntity.ok(apiResponse);
	}
	
	@GetMapping(value = "/grnDetailsByChassisNo")
	public ResponseEntity<?> getGrnDetailsByChassisNo(@RequestParam("chassisNo") String chassisNo,

			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		/*CheckDraftModeDto draftModeDto = pdiAggregateRepo.servicePdiDraftModeCheck(chassisNo, userCode);
		if (draftModeDto.getIsTrue() == Boolean.TRUE) {
			apiResponse.setMessage("Chassis Number is Already in draft mode");
			apiResponse.setStatus(HttpStatus.OK.value());
			apiResponse.setResult(draftModeDto);
		} else {*/
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			System.out.println("chassisNo--" + chassisNo + "----");
			apiResponse.setResult(pdiAggregateRepo.grnDetailsByChassisNo(chassisNo,userCode));
	//	}
		return ResponseEntity.ok(apiResponse);
	}
	
	
	 @SuppressWarnings("rawtypes")
	@PostMapping(value = "/outwardPdiSearch")
	    public ResponseEntity<?> pdiSearch(@RequestBody PdiSearchDto pdiSearchDto,@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
				OAuth2Authentication authentication) {
		 
		 String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
	        ApiResponse apiResponse = new ApiResponse();
	        List<PdiSearchResponse> result = pdiRepository.pdiSearch(pdiSearchDto.pdiFromDate,
	                pdiSearchDto.pdiToDate, pdiSearchDto.chassisNo,
	                pdiSearchDto.engineNo,
	                pdiSearchDto.dmsGrnFromDate,
	                pdiSearchDto.dmsGrnToDate,
	                pdiSearchDto.pdiNumber,
	                pdiSearchDto.page,
	                pdiSearchDto.size,
	                pdiSearchDto.invoiceNumber,
	                userCode
	               );
	        apiResponse.setMessage("Pdi search list");
	        apiResponse.setStatus(HttpStatus.OK.value());
	        apiResponse.setResult(result);
	        Long count = 0L;
	        if(result!=null && result.size()>0){
	        	apiResponse.setCount(result.get(0).getRecordCount());
	        	//apiResponse.setCount(Long.valueOf(result.size()));
	        }
	        return ResponseEntity.ok(apiResponse);
	    }
	 
	 @GetMapping(value = "/search/autocompleteChassis")
	    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> searchAutoCompleteChassisNumber(@RequestParam String searchString,
	    		@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
				OAuth2Authentication authentication) {
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
	        ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();
	        apiResponse.setMessage("ChassisNo auto complete search list");
	        apiResponse.setStatus(HttpStatus.OK.value());

	        apiResponse.setResult(
	                pdiRepository.searchAutocompleteChassisNumber(searchString, userCode));
	        return ResponseEntity.ok(apiResponse);

	    }
}
