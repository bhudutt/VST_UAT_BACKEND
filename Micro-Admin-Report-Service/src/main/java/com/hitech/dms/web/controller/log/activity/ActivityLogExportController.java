/**
 * 
 */
package com.hitech.dms.web.controller.log.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.ExcelUtils;
import com.hitech.dms.web.dao.log.activity.ActivityLogDao;
import com.hitech.dms.web.model.export.response.ExportResponseModel;
import com.hitech.dms.web.model.log.activity.list.request.ActivityLogListRequestModel;
import com.hitech.dms.web.model.log.activity.list.response.ActivityLogListMainResponseModel;
import com.hitech.dms.web.model.log.activity.list.response.ActivityLogListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class ActivityLogExportController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogExportController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private ActivityLogDao activityLogDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@PostMapping("/fetchAcivityLogExport")
	public ResponseEntity<?> fetchAcivityLogExport(@RequestBody ActivityLogListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ExportResponseModel responseModel = null;
		String fileName = null;
		ActivityLogListMainResponseModel responseMainModel = activityLogDao.fetchAcivityLogExport(userCode,
				requestModel);
		if (responseMainModel != null) {
			List<ActivityLogListResponseModel> exportList = responseMainModel.getSearchList();

			Date currDate = new Date();

			fileName = messageSource.getMessage("app.temp.file.name",
					new Object[] { "ACTIVITYLOGLIST" + "_" + currDate.getTime() }, LocaleContextHolder.getLocale());
			String filePath = messageSource.getMessage("label.temp.activity.log.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
			String limit = messageSource.getMessage("label.rep.activity.log.excelLimit", new Object[] { 1 },
					LocaleContextHolder.getLocale());
			if (exportList == null || exportList.isEmpty()) {
				exportList = new ArrayList<ActivityLogListResponseModel>();
				ActivityLogListResponseModel exportModel = new ActivityLogListResponseModel();
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
				List<List<ActivityLogListResponseModel>> parts = CommonUtils.chopIntoParts(exportList,
						target.intValue());
				logger.debug("parts : " + parts.size());
				for (int i = 1; i <= parts.size(); i++) {
					responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
							messageSource.getMessage("label.rep.access.log.list.sheet", new Object[] { i },
									LocaleContextHolder.getLocale()),
							"", messageSource.getMessage("label.rep.access.log.list.header", null,
									LocaleContextHolder.getLocale()),
							parts.get(i - 1), "EXPORTREPACTLOGLIST");
				}
			} else {
				responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
						messageSource.getMessage("label.rep.access.log.list.sheet", new Object[] { 1 },
								LocaleContextHolder.getLocale()),
						"", messageSource.getMessage("label.rep.access.log.list.header", null,
								LocaleContextHolder.getLocale()),
						exportList, "EXPORTREPACTLOGLIST");
			}
			logger.info("fileName : " + fileName);
		}
		if (responseModel != null && responseModel.isFileCreated()) {
			responseModel.setFileName(fileName);
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Activity Log List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
