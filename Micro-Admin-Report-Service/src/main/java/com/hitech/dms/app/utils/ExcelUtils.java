/**
 * 
 */
package com.hitech.dms.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hitech.dms.web.model.export.response.ExportResponseModel;

@Component
public class ExcelUtils {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

	public static <T> Map<String, ?> writeToExcelInMultiSheets(final String filePath, String fileName,
			final String sheetName, final String fertCode, String header, final List<T> data, String isFor) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		ByteArrayOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		String isCreated = "CREATED";
		logger.debug(fertCode + " : " + filePath + " : fileName : " + fileName);
		Sheet sheet = null;
		CellStyle style = null;
		List<String> fieldNames = null;
		Font font = null;
		try {
			workbook = new XSSFWorkbook();
			int sheetIndex = workbook.getSheetIndex(sheetName);
			if (sheetIndex == -1) {
				sheet = workbook.createSheet(sheetName);
			} else {
				workbook.removeSheetAt(sheetIndex);
				sheet = workbook.createSheet(sheetName);
			}
			logger.debug("sheet created");
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.TOP);
			style.setBottomBorderColor((short) 16);
			font = workbook.createFont();
			font.setColor(IndexedColors.BLACK.getIndex());
			font.setBold(true);
//            font.setBoldweight((short) 16);
			style.setFont(font);
			logger.debug("style sheet created");
			fieldNames = getFieldNamesForClass(data.get(0).getClass());
			int rowCount = 0;
			int columnCount = 0;
			Row row = sheet.createRow(rowCount++);
			int lastCol = fieldNames.size() - 2;
			Cell headerCell = row.createCell(columnCount);
			if (header != null || fertCode != null) {
				headerCell.setCellValue(header + "" + (fertCode == null ? "" : fertCode)); // new
																							// XSSFRichTextString(fertCode)
			}
			headerCell.setCellStyle(style);
			if (isFor != null && (isFor.equalsIgnoreCase("EXPORTCART") || isFor.equalsIgnoreCase("EXPORTREPPARTLIST")
					|| isFor.equalsIgnoreCase("EXPORTREPOBSLIST") || isFor.equalsIgnoreCase("DEALERMAPPINGLIST")
					|| isFor.equalsIgnoreCase("DEALERMAPPINGLIST") || isFor.equalsIgnoreCase("EXPORTFERTDASHLIST"))) {
				lastCol = fieldNames.size() - 1;
			} else if (isFor != null
					&& (isFor.equalsIgnoreCase("EXPORTREPENQLIST") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST"))) {
				lastCol = fieldNames.size() - 3;
			} else if (isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")) {
				lastCol = fieldNames.size() - 4;
			}
			if (isFor != null && (isFor.equalsIgnoreCase("BOM") || isFor.equalsIgnoreCase("EXPORTREPPOLIST")
					|| isFor.equalsIgnoreCase("EXPORTREPPOLIST"))) {
				lastCol = fieldNames.size() - 3;
			}
			logger.debug("header sheet created");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
			// header style
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setVerticalAlignment(VerticalAlignment.TOP);
			font = workbook.createFont();
			font.setColor(IndexedColors.BLACK.getIndex());
			style.setFont(font);
			row = sheet.createRow(rowCount++);
			logger.debug("creating row header");
			Cell cell;
			for (String fieldName : fieldNames) {
				if (fieldName != null
						&& (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("sno"))) {
					continue;
				} else if (fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("BOM") && fieldName.equals("modelNo")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPENQLIST") && (fieldName.equals("enquiryId")
						|| fieldName.equals("totalRecords") || fieldName.equals("action"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")
						&& (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && fieldName.equals("effDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")
						&& fieldName.equals("effectiveDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
						&& (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked")
								|| fieldName.equals("createdBy") || fieldName.equals("createdDate"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN") && fieldName.equals("issueDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("KIT") && fieldName.equals("issueDate")) {
					continue;
				}
				cell = row.createCell(columnCount++);
				cell.setCellStyle(style);
				sheet.autoSizeColumn(cell.getColumnIndex());
				cell.setCellValue(resetFieldsName(fieldName));
				fieldName = null;
			}
			logger.debug("row header created");
			XSSFCreationHelper createHelper = null;
			XSSFCellStyle cellStyle = null;
			if (isFor != null && (isFor.equalsIgnoreCase("EXPORTREPCHLIST")
					|| isFor.equalsIgnoreCase("EXPORTECNMBOMLIST") || isFor.equalsIgnoreCase("BULLETIN"))) {
				// 1. Create the date cell style
				createHelper = workbook.getCreationHelper();
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
			}
			Class<? extends Object> classz = data.get(0).getClass();
			for (T t : data) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				for (String fieldName : fieldNames) {
					// logger.debug("fieldName : "+fieldName);
					cell = row.createCell(columnCount);
					Method method = null;
					if (fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo")
							|| fieldName.equals("modelNo") || fieldName.equals("sno"))) {
						continue;
					} else if (fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("BOM") && fieldName.equals("modelNo")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPENQLIST")
							&& (fieldName.equals("enquiryId") || fieldName.equals("totalRecords")
									|| fieldName.equals("action"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")
							&& (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")
							&& fieldName.equals("effDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")
							&& fieldName.equals("effectiveDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
							&& (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked")
									|| fieldName.equals("createdBy") || fieldName.equals("createdDate"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN") && fieldName.equals("issueDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("KIT") && fieldName.equals("issueDate")) {
						continue;
					}
					try {
						method = classz.getMethod("get" + capitalize(fieldName));
					} catch (NoSuchMethodException nme) {
						isCreated = "NTCREATED";
						method = classz.getMethod("get" + fieldName);
					}
					Object value = method.invoke(t, (Object[]) null);
					// logger.debug("value : "+value);
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else {
							cell.setCellValue("" + value); // Deepak managal Sir
						}
						if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")
								&& (fieldName.equals("effectiveDate") || fieldName.equals("EFFECTIVE DATE"))
								&& cellStyle != null) {
							cell.setCellStyle(cellStyle);
						} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
								&& fieldName.equals("effDate")) {
							cell.setCellStyle(cellStyle);
						} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN")
								&& (fieldName.equals("issueDateValue") || fieldName.equals("ISSUE DATE"))) {
							cell.setCellStyle(cellStyle);
						}
					}
//                    sheet.autoSizeColumn(cell.getColumnIndex());
//                    logger.debug(value+" : columnCount : "+columnCount+" : rowCount : "+rowCount);
					columnCount++;
					fieldName = null;
				}
			}
			logger.debug("excel created");
			outputStream = new ByteArrayOutputStream();
			logger.debug("file writing in WorkBook..");
			workbook.write(outputStream);
			outputStream.flush();
			logger.debug("excel created successfully");
		} catch (Exception e) {
			isCreated = "NTCREATED";
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				isCreated = "NTCREATED";
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				isCreated = "NTCREATED";
			}
			sheet = null;
			workbook = null;
			style = null;
			fieldNames = null;
			font = null;
			mapData.put("isCreated", isCreated);
			if (isCreated != null && !isCreated.equals("NTCREATED")) {
				mapData.put("InputStream", new ByteArrayInputStream(outputStream.toByteArray()));
			}
		}
		return mapData;
	}

	public static <T> ExportResponseModel createExcelInMultiSheets(final String filePath, String fileName,
			final String sheetName, final String fertCode, String header, final List<T> data, String isFor) {
		File file = null;
		OutputStream fos = null;
		XSSFWorkbook workbook = null;
		logger.debug(fertCode + " : " + filePath + " : fileName : " + fileName);
		File directory = null;
		Sheet sheet = null;
		CellStyle style = null;
		List<String> fieldNames = null;
		Font font = null;
		ExportResponseModel responseModel = new ExportResponseModel();
		responseModel.setFileCreated(false);
		try {
			directory = new File(filePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			fileName = filePath + fileName;
			file = new File(fileName);
			logger.debug("fileName : : : " + fileName);
			file.setReadable(true, false);
			if (file.exists()) {
				workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
			} else {
				workbook = new XSSFWorkbook();
			}
			int sheetIndex = workbook.getSheetIndex(sheetName);
			if (sheetIndex == -1) {
				sheet = workbook.createSheet(sheetName);
			} else {
				workbook.removeSheetAt(sheetIndex);
				sheet = workbook.createSheet(sheetName);
			}
			logger.debug("sheet created");
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.TOP);
			style.setBottomBorderColor((short) 16);
			font = workbook.createFont();
			font.setColor(IndexedColors.BLACK.getIndex());
			font.setBold(true);
//            font.setBoldweight((short) 16);
			style.setFont(font);
			logger.debug("style sheet created");
			fieldNames = getFieldNamesForClass(data.get(0).getClass());
			int rowCount = 0;
			int columnCount = 0;
			Row row = sheet.createRow(rowCount++);
			int lastCol = fieldNames.size() - 1;
			Cell headerCell = row.createCell(columnCount);
			if (header != null || fertCode != null) {
				headerCell.setCellValue(header + "" + (fertCode == null ? "" : fertCode)); // new
																							// XSSFRichTextString(fertCode)
			}
			headerCell.setCellStyle(style);
			if (isFor != null && (isFor.equalsIgnoreCase("EXPORTCART") || isFor.equalsIgnoreCase("EXPORTREPPARTLIST")
					|| isFor.equalsIgnoreCase("EXPORTREPFERTLIST") || isFor.equalsIgnoreCase("EXPORTREPPRSQUOLIST")
					|| isFor.equalsIgnoreCase("EXPORTREPOBSLIST") || isFor.equalsIgnoreCase("DEALERMAPPINGLIST")
					|| isFor.equalsIgnoreCase("DEALERMAPPINGLIST") || isFor.equalsIgnoreCase("EXPORTFERTDASHLIST"))) {
				lastCol = fieldNames.size() - 1;
			} else if (isFor != null
					&& (isFor.equalsIgnoreCase("EXPORTREPENQLIST") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST"))) {
				lastCol = fieldNames.size() - 3;
			} else if (isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")) {
				lastCol = fieldNames.size() - 4;
			}
			logger.debug("header sheet created");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
			// header style
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setAlignment(HorizontalAlignment.LEFT);
			style.setVerticalAlignment(VerticalAlignment.TOP);
			font = workbook.createFont();
			font.setColor(IndexedColors.BLACK.getIndex());
			style.setFont(font);
			row = sheet.createRow(rowCount++);
			logger.debug("creating row header");
			Cell cell;
			for (String fieldName : fieldNames) {
				if (fieldName != null
						&& (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("sno"))) {
					continue;
				} else if (fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPPRSQUOLIST")
						&& (fieldName.equals("quotationId") || fieldName.equals("totalRecords")
								|| fieldName.equals("action"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPENQLIST") && (fieldName.equals("enquiryId")
						|| fieldName.equals("totalRecords") || fieldName.equals("action"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")
						&& (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && fieldName.equals("effDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")
						&& fieldName.equals("effectiveDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
						&& (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked")
								|| fieldName.equals("createdBy") || fieldName.equals("createdDate"))) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN") && fieldName.equals("issueDate")) {
					continue;
				} else if (isFor != null && isFor.equalsIgnoreCase("KIT") && fieldName.equals("issueDate")) {
					continue;
				}
				cell = row.createCell(columnCount++);
				cell.setCellStyle(style);
				sheet.autoSizeColumn(cell.getColumnIndex());
				cell.setCellValue(resetFieldsName(fieldName));
				fieldName = null;
			}
			logger.debug("row header created");
			XSSFCreationHelper createHelper = null;
			XSSFCellStyle cellStyle = null;
			if (isFor != null && (isFor.equalsIgnoreCase("EXPORTREPCHLIST")
					|| isFor.equalsIgnoreCase("EXPORTECNMBOMLIST") || isFor.equalsIgnoreCase("BULLETIN"))) {
				// 1. Create the date cell style
				createHelper = workbook.getCreationHelper();
				cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
			}
			Class<? extends Object> classz = data.get(0).getClass();
			for (T t : data) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				for (String fieldName : fieldNames) {
					// System.out.println("fieldName : "+fieldName);
					cell = row.createCell(columnCount);
					Method method = null;
					if (fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo")
							|| fieldName.equals("modelNo") || fieldName.equals("sno"))) {
						continue;
					} else if (fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPPRSQUOLIST")
							&& (fieldName.equals("quotationId") || fieldName.equals("totalRecords")
									|| fieldName.equals("action"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPENQLIST")
							&& (fieldName.equals("enquiryId") || fieldName.equals("totalRecords")
									|| fieldName.equals("action"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")
							&& (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")
							&& fieldName.equals("effDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")
							&& fieldName.equals("effectiveDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
							&& (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked")
									|| fieldName.equals("createdBy") || fieldName.equals("createdDate"))) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN") && fieldName.equals("issueDate")) {
						continue;
					} else if (isFor != null && isFor.equalsIgnoreCase("KIT") && fieldName.equals("issueDate")) {
						continue;
					}
					try {
						method = classz.getMethod("get" + capitalize(fieldName));
					} catch (NoSuchMethodException nme) {
						responseModel.setFileCreated(false);
						method = classz.getMethod("get" + fieldName);
					}
					Object value = method.invoke(t, (Object[]) null);
					// logger.debug("value : "+value);
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else {
							cell.setCellValue("" + value);
						}
						if (isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")
								&& (fieldName.equals("effectiveDate") || fieldName.equals("EFFECTIVE DATE"))
								&& cellStyle != null) {
							cell.setCellStyle(cellStyle);
						} else if (isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")
								&& fieldName.equals("effDate")) {
							cell.setCellStyle(cellStyle);
						} else if (isFor != null && isFor.equalsIgnoreCase("BULLETIN")
								&& (fieldName.equals("issueDateValue") || fieldName.equals("ISSUE DATE"))) {
							cell.setCellStyle(cellStyle);
						}
					}
//                    sheet.autoSizeColumn(cell.getColumnIndex());
//                   logger.debug(value+" : columnCount : "+columnCount+" : rowCount : "+rowCount);
					columnCount++;
					fieldName = null;
				}
			}
			logger.debug("excel created");
			fos = new FileOutputStream(file);
			logger.debug("file writing in WorkBook..");
			workbook.write(fos);
			fos.flush();
			responseModel.setFileCreated(true);
			logger.debug("excel created successfully");
		} catch (Exception e) {
			responseModel.setFileCreated(false);
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				responseModel.setFileCreated(false);
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				responseModel.setFileCreated(false);
			}
			directory = null;
			sheet = null;
			workbook = null;
			style = null;
			fieldNames = null;
			font = null;
			if (responseModel.isFileCreated()) {
				responseModel.setMsg("File Has Been Created Successfully.");
			} else {
				responseModel.setMsg("Error While Creating Export File.");
			}
		}
		logger.debug("responseModel : " + responseModel);
		return responseModel;
	}

	// retrieve field names from a POJO class
	private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
		List<String> fieldNames = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	// capitalize the first letter of the field name for retriving value of the
	// field later
	private static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	private static String resetFieldsName(String fieldName) {
		if (fieldName.equalsIgnoreCase(ModalPropertyEnum.LEVEL.getPropertyName())) {
			fieldName = ModalPropertyEnum.LEVEL.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USERCODE.getPropertyName())) {
			fieldName = ModalPropertyEnum.USERCODE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.IP.getPropertyName())) {
			fieldName = ModalPropertyEnum.IP.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.LONGITUDE.getPropertyName())) {
			fieldName = ModalPropertyEnum.LONGITUDE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.LATITUDE.getPropertyName())) {
			fieldName = ModalPropertyEnum.LATITUDE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.QTY.getPropertyName())) {
			fieldName = ModalPropertyEnum.QTY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.EVENT.getPropertyName())) {
			fieldName = ModalPropertyEnum.EVENT.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PAGE.getPropertyName())) {
			fieldName = ModalPropertyEnum.PAGE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SRLNO.getPropertyName())) {
			fieldName = ModalPropertyEnum.SRLNO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USERNAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.USERNAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CITY.getPropertyName())) {
			fieldName = ModalPropertyEnum.CITY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.EMAIL.getPropertyName())) {
			fieldName = ModalPropertyEnum.EMAIL.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MOBILENO.getPropertyName())) {
			fieldName = ModalPropertyEnum.MOBILENO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_NAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.USER_NAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.LOGGEDTIME.getPropertyName())) {
			fieldName = ModalPropertyEnum.LOGGEDTIME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USERAGENT.getPropertyName())) {
			fieldName = ModalPropertyEnum.USERAGENT.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MRP.getPropertyName())) {
			fieldName = ModalPropertyEnum.MRP.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.VEHICLE_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.VEHICLE_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SO_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.SO_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MFGDATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.MFGDATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.VALID_FROM.getPropertyName())) {
			fieldName = ModalPropertyEnum.VALID_FROM.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PART.getPropertyName())) {
			fieldName = ModalPropertyEnum.OLD_PART.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PARTDESC.getPropertyName())) {
			fieldName = ModalPropertyEnum.OLD_PARTDESC.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_QTY.getPropertyName())) {
			fieldName = ModalPropertyEnum.OLD_QTY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PART.getPropertyName())) {
			fieldName = ModalPropertyEnum.NEW_PART.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PARTDESC.getPropertyName())) {
			fieldName = ModalPropertyEnum.NEW_PARTDESC.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_QTY.getPropertyName())) {
			fieldName = ModalPropertyEnum.NEW_QTY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_ID.getPropertyName())) {
			fieldName = ModalPropertyEnum.USER_ID.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DESCRIPTION.getPropertyName())) {
			fieldName = ModalPropertyEnum.DESCRIPTION.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ADDRESS1.getPropertyName())) {
			fieldName = ModalPropertyEnum.ADDRESS1.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ADDRESS2.getPropertyName())) {
			fieldName = ModalPropertyEnum.ADDRESS2.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.STATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.STATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.COUNTRY.getPropertyName())) {
			fieldName = ModalPropertyEnum.COUNTRY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PINCODE.getPropertyName())) {
			fieldName = ModalPropertyEnum.PINCODE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CONTACT_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.CONTACT_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MOBILE_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.MOBILE_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.REG_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.REG_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.STATUS.getPropertyName())) {
			fieldName = ModalPropertyEnum.STATUS.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_FULL_NAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.USER_FULL_NAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.Quantity.getPropertyName())) {
			fieldName = ModalPropertyEnum.Quantity.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ChangedOn.getPropertyName())) {
			fieldName = ModalPropertyEnum.ChangedOn.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CreatedOn.getPropertyName())) {
			fieldName = ModalPropertyEnum.CreatedOn.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.PART_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_DESC.getPropertyName())) {
			fieldName = ModalPropertyEnum.PART_DESC.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.RESPONSIBILITY.getPropertyName())) {
			fieldName = ModalPropertyEnum.RESPONSIBILITY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PO_STATUS.getPropertyName())) {
			fieldName = ModalPropertyEnum.PO_STATUS.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.FROM_USER_CODE.getPropertyName())) {
			fieldName = ModalPropertyEnum.FROM_USER_CODE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.TO_USER_CODE.getPropertyName())) {
			fieldName = ModalPropertyEnum.TO_USER_CODE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.TO_USER_NAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.TO_USER_NAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MODEL_NAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.MODEL_NAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.UPLOADED_BY.getPropertyName())) {
			fieldName = ModalPropertyEnum.UPLOADED_BY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SR_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.SR_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.EFFECTIVE_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.EFFECTIVE_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PLATFORM.getPropertyName())) {
			fieldName = ModalPropertyEnum.PLATFORM.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.Series.getPropertyName())) {
			fieldName = ModalPropertyEnum.Series.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MODEL.getPropertyName())) {
			fieldName = ModalPropertyEnum.MODEL.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SERIES.getPropertyName())) {
			fieldName = ModalPropertyEnum.SERIES.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.REMARK.getPropertyName())) {
			fieldName = ModalPropertyEnum.REMARK.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENGINE_MODEL.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENGINE_MODEL.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENGINE_SERIES.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENGINE_SERIES.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.APPLICATION_TYPE.getPropertyName())) {
			fieldName = ModalPropertyEnum.APPLICATION_TYPE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ISSUE_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.ISSUE_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ISSUE_DATE_STR.getPropertyName())) {
			fieldName = ModalPropertyEnum.ISSUE_DATE_STR.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DOCUMENT_NUMBER.getPropertyName())) {
			fieldName = ModalPropertyEnum.DOCUMENT_NUMBER.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.REG_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.REG_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.MARKET.getPropertyName())) {
			fieldName = ModalPropertyEnum.MARKET.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.TotalVinCount.getPropertyName())) {
			fieldName = ModalPropertyEnum.TotalVinCount.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.LEVEL_VALUE.getPropertyName())) {
			fieldName = ModalPropertyEnum.LEVEL_VALUE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENQ_FROM.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENQ_FROM.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ISENQ_VALIDATED.getPropertyName())) {
			fieldName = ModalPropertyEnum.ISENQ_VALIDATED.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENQ_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENQ_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.VST_EXECUTIVE.getPropertyName())) {
			fieldName = ModalPropertyEnum.VST_EXECUTIVE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SALESMAN.getPropertyName())) {
			fieldName = ModalPropertyEnum.SALESMAN.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENQ_STATUS.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENQ_STATUS.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PROSPECT_TYPE.getPropertyName())) {
			fieldName = ModalPropertyEnum.PROSPECT_TYPE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SOURCE.getPropertyName())) {
			fieldName = ModalPropertyEnum.SOURCE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.STAGE.getPropertyName())) {
			fieldName = ModalPropertyEnum.STAGE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.PROFIT_CENTER.getPropertyName())) {
			fieldName = ModalPropertyEnum.PROFIT_CENTER.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.SEGMENT.getPropertyName())) {
			fieldName = ModalPropertyEnum.SEGMENT.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.VARIANT.getPropertyName())) {
			fieldName = ModalPropertyEnum.VARIANT.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ITEMNO.getPropertyName())) {
			fieldName = ModalPropertyEnum.ITEMNO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ITEM_DESC.getPropertyName())) {
			fieldName = ModalPropertyEnum.ITEM_DESC.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.FIELD_ACTIVITY_TYPE.getPropertyName())) {
			fieldName = ModalPropertyEnum.FIELD_ACTIVITY_TYPE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ACTIVITY_PLAN_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.ACTIVITY_PLAN_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ACTIVITY_PLAN_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.ACTIVITY_PLAN_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DIGITAL_PLATFORM.getPropertyName())) {
			fieldName = ModalPropertyEnum.DIGITAL_PLATFORM.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENQ_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENQ_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.ENQ_VALIDATED_STATUS.getPropertyName())) {
			fieldName = ModalPropertyEnum.ENQ_VALIDATED_STATUS.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DIGITAL_VALIDATED_BY.getPropertyName())) {
			fieldName = ModalPropertyEnum.DIGITAL_VALIDATED_BY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DIGITAL_VALIDATED_DATE.getPropertyName())) {
			fieldName = ModalPropertyEnum.DIGITAL_VALIDATED_DATE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CUST_TYPE.getPropertyName())) {
			fieldName = ModalPropertyEnum.CUST_TYPE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CUST_CODE.getPropertyName())) {
			fieldName = ModalPropertyEnum.CUST_CODE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CUST_NAME.getPropertyName())) {
			fieldName = ModalPropertyEnum.CUST_NAME.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.CUST_MOBILE_NO.getPropertyName())) {
			fieldName = ModalPropertyEnum.CUST_MOBILE_NO.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.COMPANY.getPropertyName())) {
			fieldName = ModalPropertyEnum.COMPANY.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.VILLAGE.getPropertyName())) {
			fieldName = ModalPropertyEnum.VILLAGE.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.TEHSIL.getPropertyName())) {
			fieldName = ModalPropertyEnum.TEHSIL.getDescp();
		} else if (fieldName.equalsIgnoreCase(ModalPropertyEnum.DISTRICT.getPropertyName())) {
			fieldName = ModalPropertyEnum.DISTRICT.getDescp();
		}

		return fieldName;
	}

}
