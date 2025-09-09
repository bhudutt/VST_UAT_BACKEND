package com.hitech.dms.web.controller.enquiry.digitalUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.controller.enquiry.view.EnquiryViewController;
import com.hitech.dms.web.dao.enquiry.digitalUpload.DigitalUploadDao;
import com.hitech.dms.web.dao.enquiry.digitalUpload.DigitalUploadDaoImpl;
import com.hitech.dms.web.model.enquiry.digitalReport.response.DigitalSourceResponseModel;
import com.hitech.dms.web.utils.ExcelImportManager;
import com.hitech.dms.web.utils.ExcelInvalidColumnException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */
@Validated
@RestController
@RequestMapping("/digitalUpload")
@SecurityRequirement(name = "hitechApis")
public class DigitalUploadController {

	@Autowired
	private DigitalUploadDao digitalUploadDao;

	@Autowired
	private DigitalUploadDaoImpl digitalUploadDaoImpl;
	
	@Autowired
	ResourceLoader resourceLoader;

	private static final Logger logger = LoggerFactory.getLogger(EnquiryViewController.class);

	@PostMapping("/uploadExcel")
	public ResponseEntity<?> uploadFile(@RequestParam() BigInteger digitalPlatform,
			@RequestParam() BigInteger profitCenter,Device device, MultipartFile file,
			OAuth2Authentication authentication) throws IOException {
	    String[] PreDefinedColumns = new String[]{"CUSTOMER NAME","MOBILE NO.","EMAIL ID","MODEL","DISTRICT","TEHSIL/TALUKA/MANDAL","STATE","SEGMENT"};
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String userCode = null;
		Map<String, StringBuffer> excel = null;
		String msg;
		ExcelImportManager excelImportManager = new ExcelImportManager();
		boolean flag = false;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			if (!file.isEmpty()) {
				try {
					excelImportManager.checkXLSValidity(PreDefinedColumns,excelImportManager.getXLSHeaders(WorkbookFactory.create(file.getInputStream())));
				} catch (ExcelInvalidColumnException e) {
					msg = e.getMessage();
					flag = true;
					codeResponse.setCode("EC400");
					codeResponse.setDescription("Unsuccessful  on " + formatter.format(new Date()));
					codeResponse.setMessage(msg);
				}
				if (!flag) {
					excel = digitalUploadDao.uploadExcel(userCode, digitalPlatform, profitCenter, device, file);
					codeResponse.setCode("EC200");
					codeResponse.setDescription("Uploaded  on " + formatter.format(new Date()));
					if (excel.get("error") != null && excel.get("error").toString().equals("")) {
						codeResponse.setMessage("Template Uploaded Successfully");	
					}else {
						codeResponse.setMessage((excel.get("error")).toString());	
					}
				}
			}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(excel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/downloadTemplate")
	public ResponseEntity<InputStreamResource> downloadTemplate(@RequestParam("filename") String filename,
			HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException {
		File fileOnServer = null;
		FileInputStream fileInputStream = null;
		InputStream inputStream = null;
		HttpHeaders headers = new HttpHeaders();
		try {
//			ClassPathResource cpr = new ClassPathResource("template/" + filename);
//			Resource resource = new DefaultResourceLoader().getResource("classpath:template/" + filename);
			inputStream = getClass().getResourceAsStream("/template/" + filename);
//	        File file = ResourceUtils.getFile("classpath:template/" + filename);
	        
//			fileOnServer = Paths.get(cpr.getURI()).toFile();
//			fileOnServer = Paths.get(resource.getURI()).toFile();
//			fileOnServer = new File(fileOnServer.getAbsolutePath());
//			inputStream = new FileInputStream(fileOnServer);
			response.setContentType("application/vnd.ms-excel");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream));

	}

	@GetMapping("/getDigitalSource")
	public ResponseEntity<?> fetchEnqSourcesList(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		List<DigitalSourceResponseModel> digitalSourceList = digitalUploadDaoImpl.getDigitalSource(userCode);
		if (digitalSourceList!=null) {
			codeResponse.setDescription("Digital Source " + formatter.format(new Date()));
			codeResponse.setMessage("Digital Source Successfully");
		}else {
			codeResponse.setDescription("Digital Source  Data not available" + formatter.format(new Date()));
			codeResponse.setMessage("Digital Source fetch unsuccessfully");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(digitalSourceList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
