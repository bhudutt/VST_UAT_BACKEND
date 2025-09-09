/**
 * 
 */
package com.hitech.dms.web.dao.incentive.template.billing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.scheme.template.billing.download.request.SchemeIncentiveTemplateRequestModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.request.IncentiveBillingRequestModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.response.IncentiveBillingMainResponseModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.response.IncentiveBillingResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IncentiveTemplateDaoImpl implements IncentiveTemplateDao {
	private static final Logger logger = LoggerFactory.getLogger(IncentiveTemplateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, SchemeIncentiveTemplateRequestModel requestModel) {
		logger.debug(userCode + " : " + requestModel.toString());
		logger.debug("fetching Models By PC Id : ");
		List<ModelsForSeriesSegmentResponseModel> modelsBySegmentList = commonDao.fetchModelsForSeriesSegment(
				authorizationHeader, requestModel.getPcId(), requestModel.getSeriesName(), null);
//		List<ModelByPcIdResponseModel> modelByPcIdResponseList = commonDao.fetchModelListByPcId(authorizationHeader,
//				requestModel.getPcId(), requestModel.getIsFor());
//		if (modelByPcIdResponseList == null) {
//			return null;
//		}
		if (modelsBySegmentList == null) {
			return null;
		}
		long start = System.currentTimeMillis();
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Billing Scheme Type");

		ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("MODEL", "INCENTIVE TYPE", "DEALER CATEGORY",
				"INCENTIVE AMT", "QUUALIFYING CRITERIA", "DELIVERY CRITERIA"));

		// Create cell style for header row
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.BLACK.getIndex());
		style.setFont(font);
		int rownum = 0;
		int cellnum = 0;
		Row row = sheet.createRow(rownum++);
		for (String obj : staticHeaderList) {
			Cell cell = row.createCell(cellnum++);
			if (obj instanceof String)
				cell.setCellValue((String) obj);
			else
				cell.setCellValue(obj == null ? "" : (String) obj);

			if (rownum == 1) { // it is header row
				cell.setCellStyle(style); // set style for header cells
			}
		}

		for (ModelsForSeriesSegmentResponseModel object : modelsBySegmentList) {
			row = sheet.createRow(rownum++);
			cellnum = 0;
			Cell cell = row.createCell(cellnum);
			if (object.getModelName() instanceof String)
				cell.setCellValue(object.getModelName());
			else
				cell.setCellValue(object.getModelName() == null ? "" : (String) object.getModelName());

			sheet.autoSizeColumn(cellnum);
			sheet.setColumnWidth(cellnum, sheet.getColumnWidth(40));
		}

		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			logger.info("XLSX creation time : " + (System.currentTimeMillis() - start));
			logger.info("IncentiveBillingTemplate.xlsx written successfully for user : " + userCode);
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

	public IncentiveBillingMainResponseModel validateIncentiveTemplate(String authorizationHeader, String userCode,
			IncentiveBillingRequestModel requestModel, List<MultipartFile> files) {
		logger.debug("validateIncentiveTemplateMethod...");
		logger.debug(userCode + " : " + requestModel.toString());
		logger.debug("fetching Models By PC Id : ");
		Session session = null;
		IncentiveBillingMainResponseModel responseModel = new IncentiveBillingMainResponseModel();
		List<IncentiveBillingResponseModel> recordList = null;
		boolean isSuccess = true;
		List<ModelsForSeriesSegmentResponseModel> modelsBySegmentList = commonDao.fetchModelsForSeriesSegment(
				authorizationHeader, requestModel.getPcId(), requestModel.getSeriesName(), null);
		if (modelsBySegmentList == null) {
			// set error msg & status code
			logger.error("Model Not Found. Kindly Contact Your Administrator.");
			responseModel.setMsg("Model Not Found. Kindly Contact Your Administrator.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			isSuccess = false;
			return responseModel;
		}
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(files.get(0).getInputStream());
			if (isSuccess) {
				XSSFSheet sheet = workbook.getSheetAt(0);
				int totalRows = sheet.getPhysicalNumberOfRows();
				Row row = sheet.getRow(0);
				short minColIx = row.getFirstCellNum();
				short maxColIx = row.getLastCellNum();
				for (int x = 1; x < totalRows - 1; x++) {
					Row dataRow = sheet.getRow(x);
					if (!isSuccess) {
						break;
					}
					for (short colIx = minColIx; colIx < maxColIx; colIx++) {
						Cell cell = dataRow.getCell(colIx);

					}
				}
			} else {
				// error

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseModel;
	}
}
