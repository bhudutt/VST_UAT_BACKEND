/**
 * 
 */
package com.hitech.dms.web.dao.aop.template.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.hibernate.AliasToEntityOrderedMapResultTransformer;
import com.hitech.dms.app.utils.FiscalDate;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.aop.template.request.AopTargetRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AopDownloadTemplateDaoImpl implements AopDownloadTemplateDao {
	private static final Logger logger = LoggerFactory.getLogger(AopDownloadTemplateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "unchecked" })
	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, AopTargetRequestModel requestModel) {
		logger.debug(userCode + " : " + requestModel.toString());
		logger.debug("fetching Models By PC Id : ");
//		ModelByPcIdResponseModel modelByPcIdResponseModel = commonDao.fetchModelListByPcId(authorizationHeader, pcId,
//				isFor);
//		if (modelByPcIdResponseModel == null) {
//			return null;
//		}
		long start = System.currentTimeMillis();
		Map<String, Object> mapData = FiscalDate.displayFinancialDate(Calendar.getInstance());
		requestModel.setFinYear((String) mapData.get("FiscalYears"));
		List<?> dbData = fetchAopTargetDtlForTemplate(userCode, requestModel);
		LinkedList<String> header = null;
		LinkedHashMap<String, Object> data = null;
		if (dbData != null && !dbData.isEmpty()) {
			for (Object object : dbData) {
				data = (LinkedHashMap<String, Object>) object;
				header = (LinkedList<String>) data.keySet().stream()
						.filter(m -> !m.toString().equalsIgnoreCase("Machine_Item_Id")).map(m -> {
							if (m.toString().equalsIgnoreCase("FinYear")) {
								m = (String) mapData.get("FiscalYears");
							}
							Matcher numberMatcher = Pattern.compile("[^0-9]*([0-9]+).*").matcher(m.toString());
							if (m.toString().length() > 9 && numberMatcher.matches()) {
								int index = m.toString().indexOf(numberMatcher.group(1));
								m = m.toString().substring(0, index);
							}
							return m.toString().toUpperCase();
						}).collect(Collectors.toCollection(LinkedList::new));
				break;
			}

			// Blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();

			// Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("AOP TARGET");

//			data.put("1", combinedHeaderList.toArray());

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
			for (String obj : header) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else
					cell.setCellValue(obj == null ? "" : (String) obj);

				if (rownum == 1) { // it is header row
					cell.setCellStyle(style); // set style for header cells
				}
			}
			logger.info("XLSX creation time Before loop : " + (System.currentTimeMillis() - start));
//			rownum = 1;
			for (Object object : dbData) {
				row = sheet.createRow(rownum++);
				Map<String, Object> rowData = (Map<String, Object>) object;
				cellnum = 0;
				for (Map.Entry<String, Object> entry : rowData.entrySet()) {
					if(entry.getKey().equalsIgnoreCase("Machine_Item_Id")) {
						continue;
					}
					Object obj = entry.getValue();
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
					else if (obj instanceof Boolean)
						cell.setCellValue((Boolean) obj);
					else if (obj instanceof BigInteger)
						cell.setCellValue(((BigInteger) obj).longValue());
					else if (obj instanceof BigDecimal)
						cell.setCellValue(((BigDecimal) obj).doubleValue());
					else
						cell.setCellValue(obj == null ? "" : (String) obj);

//					if (rownum == 1) { // it is header row
//						cell.setCellStyle(style); // set style for header cells
//					}
					sheet.autoSizeColumn(cellnum);
					sheet.setColumnWidth(cellnum, sheet.getColumnWidth(40));
				}
			}
			ByteArrayOutputStream outputStream = null;
			try {
				outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				logger.info("XLSX creation time : " + (System.currentTimeMillis() - start));
				logger.info("aop_target_plans.xlsx written successfully for user : " + userCode);
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
		return null;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchAopTargetDtlForTemplate(String userCode, AopTargetRequestModel requestModel) {
		Session session = null;
		String sqlQuery = "exec [SP_SA_MIS_AOP_TARGET_SEARCH] :userCode, :pcId, :finYear, :lastFinYearCount, :aopId";
		List data = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("finYear", requestModel.getFinYear());
			query.setParameter("lastFinYearCount", requestModel.getLastFinYearCount());
			query.setParameter("aopId", requestModel.getAopId());
			query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
			data = query.list();

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return data;
	}

//	@SuppressWarnings({ "deprecation", "rawtypes" })
//	public List<AopTargetResponseModel> fetchAopTargetDtlForTemplate(String userCode,
//			AopTargetRequestModel requestModel) {
//		Session session = null;
//		Query query = null;
//		List<AopTargetResponseModel> responseModelList = null;
//		String sqlQuery = "select TAR.*, HDR.aop_fin_year FinYear, MI.item_no Item, MI.item_description ItemDesc, MI.variant Variant, "
//				+ " MM.series_name Series, MM.segment_name Segment, MM.model_name Model "
//				+ " from SA_MIS_AOP_TARGET_DTL (nolock) TAR "
//				+ " inner join SA_MIS_AOP_TARGET_HDR (nolock) HDR on TAR.aop_id = HDR.aop_id "
//				+ " left join CM_MST_MACHINE_ITEM (nolock) MI on TAR.machine_item_id = MI.machine_item_id "
//				+ " left join CM_MST_MODEL (nolock) MM on MI.model_id = MM.model_id and MM.pc_id =:pcId "
//				+ " where HDR.pc_id =:pcId";
//		try {
//			session = sessionFactory.openSession();
//			query = session.createNativeQuery(sqlQuery);
//			query.setParameter("pcId", requestModel.getPcId());
//			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//			List data = query.list();
//			if (data != null && !data.isEmpty()) {
//				responseModelList = new ArrayList<AopTargetResponseModel>();
//				for (Object object : data) {
//					Map row = (Map) object;
//					AopTargetResponseModel model = new AopTargetResponseModel();
//					model.setSeries((String) row.get("Series"));
//					model.setSegment((String) row.get("Segment"));
//					model.setVariant((String) row.get("Variant"));
//					model.setModel((String) row.get("Model"));
//					model.setItem((String) row.get("Item"));
//					model.setItemDesc((String) row.get("ItemDesc"));
//					model.setFinYear((String) row.get("FinYear"));
//					model.setMonth1((BigDecimal) row.get("month_1"));
//					model.setMonth2((BigDecimal) row.get("month_2"));
//					model.setMonth3((BigDecimal) row.get("month_3"));
//					model.setMonth4((BigDecimal) row.get("month_4"));
//					model.setMonth5((BigDecimal) row.get("month_5"));
//					model.setMonth6((BigDecimal) row.get("month_6"));
//					model.setMonth7((BigDecimal) row.get("month_7"));
//					model.setMonth8((BigDecimal) row.get("month_8"));
//					model.setMonth9((BigDecimal) row.get("month_9"));
//					model.setMonth10((BigDecimal) row.get("month_10"));
//					model.setMonth11((BigDecimal) row.get("month_11"));
//					model.setMonth12((BigDecimal) row.get("month_12"));
//
//					responseModelList.add(model);
//				}
//			}
//		} catch (SQLGrammarException ex) {
//			logger.error(this.getClass().getName(), ex);
//		} catch (Exception ex) {
//			logger.error(this.getClass().getName(), ex);
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//		return responseModelList;
//	}
}
