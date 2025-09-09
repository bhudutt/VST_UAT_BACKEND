/**
 * 
 */
package com.hitech.dms.web.dao.activityplan.upload.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.dao.dealer.DealerListUnderUserDao;
import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;
import com.hitech.dms.web.model.dealer.UserDealerResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPlanTemplateDaoImpl implements ActivityPlanTemplateDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanTemplateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonServiceClient commonServiceClient;
	@Autowired
	private DealerListUnderUserDao dealerListUnderUserDao;

	public List<ActivitySourceListResponseModel> fetchActivitySourceList(String authorizationHeader, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceList invoked..");
		}
		List<ActivitySourceListResponseModel> activitySourceList = null;
		try {
//			HeaderResponse headerResponse = commonServiceClient.fetchActivitySourceListByPcId(authorizationHeader,
//					pcId);
			ActivitySourceListRequestModel requestFormModel = new ActivitySourceListRequestModel();
			requestFormModel.setPcId(pcId);
			requestFormModel.setIsFor("Activity");
			requestFormModel.setIsIncludeInActive("Y");
			HeaderResponse headerResponse = commonServiceClient.fetchActivitySourceListByPcId(authorizationHeader,
					requestFormModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					activitySourceList = jsonArrayToObjectList(jsonString, ActivitySourceListResponseModel.class);
					logger.debug((activitySourceList == null ? "Activity Source Not Found." : activitySourceList.toString()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return activitySourceList;
	}

//	List<UserBranchListModel> branchList =

	public ByteArrayInputStream createActivityPlanTemplateExcel(HttpServletResponse response, String userCode,
			String authorizationHeader, Integer pcId, String isInactiveInclude) {
		logger.debug(userCode + " : "+ pcId + " : "+isInactiveInclude);
		List<ActivitySourceListResponseModel> activitySourceList = fetchActivitySourceList(authorizationHeader, pcId);
		List<UserDealerResponseModel> usrDealerList = dealerListUnderUserDao.fetchUserDealerListNew(authorizationHeader,
				isInactiveInclude,pcId,userCode);
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Activity Plans");

		ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("STATE", "DEALER CODE", "DEALER NAME",
				"MODEL", "PLAN DATE", "DELIVERY TARGET", "CONVERSION RATION", "VST SHARE %", "BILLING PLAN"));

		List<String> activityDescList = activitySourceList.stream().map(e -> e.getActivityDesc())
				.collect(Collectors.toList());

		List<String> combinedHeaderList = Stream.of(staticHeaderList, activityDescList).flatMap(x -> x.stream())
				.collect(Collectors.toList());

//		String headerCommaSeparated = String.join(",", combinedHeaderList);
		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", combinedHeaderList.toArray());
		if (usrDealerList != null && !usrDealerList.isEmpty()) {
			int i = 2;
			for (UserDealerResponseModel dlrListModel : usrDealerList) {
				data.put(i + "", new Object[] { dlrListModel.getDealerLocation(), dlrListModel.getDealerCode(),
						dlrListModel.getDealerName(),"","","","",50});
				i++;
			}
		}

		// Create cell style for header row
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.BLACK.getIndex());
		style.setFont(font);

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
				else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else
					cell.setCellValue(obj == null ? "" : (String) obj);

				if (rownum == 1) { // it is header row
					cell.setCellStyle(style); // set style for header cells
				}
				sheet.autoSizeColumn(cellnum);
				sheet.setColumnWidth(cellnum, sheet.getColumnWidth(cellnum) * 19 / 10);
			}
		}

//		try (// Write the workbook in file system
//				FileOutputStream out = new FileOutputStream(new File("activity_plans.xlsx"));) {
//		workbook.write(out);
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			System.out.println("activity_plans.xlsx written successfully for user : " + userCode);
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e1);
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		}
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}

	@Override
	public ByteArrayInputStream createActivityPlanTemplateExcel(HttpServletResponse response, String userCode,
			String authorizationHeader, ActivityPlanUploadRequestModel activityPlanUploadRequestModel) {
		return createActivityPlanTemplateExcel(response, userCode, authorizationHeader,
				activityPlanUploadRequestModel.getPcID(), activityPlanUploadRequestModel.getIsInactiveInclude());

	}
}
