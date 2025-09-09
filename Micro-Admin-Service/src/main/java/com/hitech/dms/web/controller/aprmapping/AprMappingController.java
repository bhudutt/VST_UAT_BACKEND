package com.hitech.dms.web.controller.aprmapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.aprmapping.dao.AprMappingDao;
import com.hitech.dms.web.model.admin.aprmapping.AprMappingRequestModel;
import com.hitech.dms.web.model.admin.aprmapping.AprMappingResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/apr")
@SecurityRequirement(name = "hitechApis")
public class AprMappingController {
	
	private static final Logger logger = LoggerFactory.getLogger(AprMappingController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@Autowired 
	private AprMappingDao aprMappingDao;
	
	@Value("${file.upload-dir.AprMappingTemplate}")
    private String templateDownloadPath;
	
	@GetMapping(value = "/downloadTemplate")
	public ResponseEntity downloadTemplate(@RequestParam("filename") String filename,
			HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		Resource resource = null;
		try {
			
			
			String filePath="";
			String property = System.getProperty("os.name");
			if(property.contains("Windows")) {
				filePath=templateDownloadPath;
			}else {
				filePath="/var/VST-DMS-APPS/FILES/Template/APR Template/";
			}
			
			
			Path path = Paths.get( filePath+ filename);
			
			resource = new UrlResource(path.toUri());
			
//			ClassPathResource cpr = new ClassPathResource("template/" + filename);
//			Resource resource = new DefaultResourceLoader().getResource("classpath:template/" + filename);
			//inputStream = getClass().getResourceAsStream(templateDownloadPath+filename);
	        //File file = ResourceUtils.getFile(templateDownloadPath+filename);
	        
//			fileOnServer = Paths.get(cpr.getURI()).toFile();
//			fileOnServer = Paths.get(resource.getURI()).toFile();
//			fileOnServer = new File(fileOnServer.getAbsolutePath());
//			inputStream = new FileInputStream(fileOnServer);
			response.setContentType("application/octet-stream");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(resource);

	}
	
	@PostMapping("/uploadExcel")
	public ResponseEntity<?> uploadFile(
			@RequestPart(value="aprUploadRequestModel") AprMappingRequestModel aprUploadRequestModel,
			@RequestPart(required = false) MultipartFile files,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, HttpServletRequest request) throws IOException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		boolean isFileExist = true;
		if (files == null ) {

			isFileExist = false;
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		AprMappingResponseModel responseModel = null;
		if(isFileExist) {
			responseModel = aprMappingDao.validateUploadedFile(authorizationHeader, userCode,
					aprUploadRequestModel, files);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Stock file.");
			}
		}else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload stock file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
