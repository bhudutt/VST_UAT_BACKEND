/**
 * 
 */
package com.hitech.dms.web.dao.opex.template.download;

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
import com.hitech.dms.web.model.opex.template.request.OpexBudgetRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OpexDownloadTemplateDaoImpl implements OpexDownloadTemplateDao {
	private static final Logger logger = LoggerFactory.getLogger(OpexDownloadTemplateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "unchecked" })
	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, OpexBudgetRequestModel requestModel) {
		logger.debug("Opex createTemplate : " + userCode + " : " + requestModel.toString());
		
		long start = System.currentTimeMillis();
		Map<String, Object> mapData = FiscalDate.displayFinancialDate(Calendar.getInstance());
		requestModel.setFinYear((String) mapData.get("FiscalYears"));
		List<?> dbData = fetchOpexBudgetDtlForTemplate(userCode, requestModel);
		LinkedList<String> header = null;
		LinkedHashMap<String, Object> data = null;
		if (dbData != null && !dbData.isEmpty()) {
			for (Object object : dbData) {
				data = (LinkedHashMap<String, Object>) object;
				header = (LinkedList<String>) data.keySet().stream()
						.filter(m -> !m.toString().equalsIgnoreCase("gl_id")).map(m -> {
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
			XSSFSheet sheet = workbook.createSheet("OPEX BUDGET");

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
			logger.info("OPEX XLSX creation time Before loop : " + (System.currentTimeMillis() - start));
//			rownum = 1;
			for (Object object : dbData) {
				row = sheet.createRow(rownum++);
				Map<String, Object> rowData = (Map<String, Object>) object;
				cellnum = 0;
				for (Map.Entry<String, Object> entry : rowData.entrySet()) {
					if(entry.getKey().equalsIgnoreCase("gl_id")) {
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
				logger.info("opex_budget_plans.xlsx written successfully for user : " + userCode);
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
	public List<?> fetchOpexBudgetDtlForTemplate(String userCode, OpexBudgetRequestModel requestModel) {
		Session session = null;
		String sqlQuery = "exec [SP_SA_MIS_OPEX_BUDGET_SEARCH] :userCode, :pcId, :finYear, :lastFinYearCount, :opexId";
		List data = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("finYear", requestModel.getFinYear());
			query.setParameter("lastFinYearCount", requestModel.getLastFinYearCount());
			query.setParameter("opexId", requestModel.getOpexId());
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
}
