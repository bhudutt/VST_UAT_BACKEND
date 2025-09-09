package com.hitech.dms.web.controller.quotation.search.export;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.ExcelUtils;
import com.hitech.dms.web.dao.quotation.search.export.QuotationSearchDao;
import com.hitech.dms.web.model.enquiry.response.EnquiryExportResponseModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class QuotationSearchController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationSearchController.class);

	@Autowired
	private QuotationSearchDao quotationSearchDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/exportPresalesQuotaionList")
	public ResponseEntity<?> exportPresalesQuotaionList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody VehQuoSearchRequestModel requestModel, OAuth2Authentication authentication,
			HttpServletResponse response) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryExportResponseModel responseModel = null;
		List<VehQuoSearchResponse> exportList = quotationSearchDao.exportQuotationList(authorizationHeader, userCode,
				requestModel);
		if (exportList != null && !exportList.isEmpty()) {
			Date currDate = new Date();

			String fileName = messageSource.getMessage("app.temp.file.name",
					new Object[] { "QUOTATIONLIST" + "_" + currDate.getTime() }, LocaleContextHolder.getLocale());
			String filePath = messageSource.getMessage("label.temp.quo.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
			String limit = messageSource.getMessage("label.rep.quo.excelLimit", new Object[] { 1 },
					LocaleContextHolder.getLocale());
			if (exportList == null || exportList.isEmpty()) {
				exportList = new ArrayList<VehQuoSearchResponse>();
				VehQuoSearchResponse exportModel = new VehQuoSearchResponse();
				exportList.add(exportModel);
			}
			filePath = fileStorageProperties.getUploadDir() + filePath;
			File file = new File(filePath + fileName);
			if (file.exists()) {
				file.delete();
			}
			if (!exportList.isEmpty()) {
				Integer listSize = exportList.size();
				logger.debug(listSize + " : limit : " + limit);
				Double target = (listSize.doubleValue()) / Integer.valueOf(limit);
				target = Math.ceil(target);
				logger.debug("target : " + target);
				List<List<VehQuoSearchResponse>> parts = CommonUtils.chopIntoParts(exportList, target.intValue());
				logger.debug("parts : " + parts.size());
				for (int i = 1; i <= parts.size(); i++) {
					responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
							messageSource.getMessage("label.rep.quo.list.sheet", new Object[] { i },
									LocaleContextHolder.getLocale()),
							"", messageSource.getMessage("label.rep.quo.list.header", null,
									LocaleContextHolder.getLocale()),
							parts.get(i - 1), "EXPORTREPPRSQUOLIST");
				}
			} else {
				responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
						messageSource.getMessage("label.rep.part.list.sheet", new Object[] { 1 },
								LocaleContextHolder.getLocale()),
						"",
						messageSource.getMessage("label.rep.quo.list.header", null, LocaleContextHolder.getLocale()),
						exportList, "EXPORTREPPRSQUOLIST");
			}
			logger.info("fileName : " + fileName);
			if (responseModel != null && responseModel.isFileCreated()) {
				responseModel.setFileName(fileName);
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Enquiry Serach List Not Fetched or server side error.");
			}
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
