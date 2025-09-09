/**
 * 
 */
package com.hitech.dms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
 
    public static <T> String writeToExcelInMultiSheets(final String filePath, String fileName, final String sheetName, final String fertCode, String header,
            final List<T> data, String isFor) {
        File file = null;
        OutputStream fos = null;
        XSSFWorkbook workbook = null;
        String isCreated = "CREATED";
        System.out.println(fertCode+" : "+filePath+" : fileName : "+fileName);
        File directory = null;
        Sheet sheet = null;
        CellStyle style = null;
        List<String> fieldNames = null;
        Font font = null;
        try {
        	directory = new File(filePath);
        	if (! directory.exists()){
                directory.mkdirs();
            }
        	fileName = filePath+"\\"+fileName;
            file = new File(fileName);
            System.out.println("fileName : : : "+fileName);
            if (file.exists()) {
                workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook();
            }
            int sheetIndex = workbook.getSheetIndex(sheetName);
            if(sheetIndex == -1){
                sheet = workbook.createSheet(sheetName);
            }else {
            	workbook.removeSheetAt(sheetIndex);
            	 sheet = workbook.createSheet(sheetName);
            }
            System.out.println("sheet created");
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
            System.out.println("style sheet created");
            fieldNames = getFieldNamesForClass(data.get(0).getClass());
            int rowCount = 0;
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            int lastCol = fieldNames.size()-2;
            Cell headerCell = row.createCell(columnCount);
            if(header != null || fertCode != null) {
            	headerCell.setCellValue(header+""+(fertCode == null ? "" : fertCode)); //new XSSFRichTextString(fertCode)
            }
            headerCell.setCellStyle(style);
            if(isFor != null && (isFor.equalsIgnoreCase("EXPORTCART")|| isFor.equalsIgnoreCase("EXPORTREPPARTLIST") || isFor.equalsIgnoreCase("EXPORTREPFERTLIST")
            		|| isFor.equalsIgnoreCase("EXPORTREPVINLIST")|| isFor.equalsIgnoreCase("EXPORTREPOBSLIST") || isFor.equalsIgnoreCase("DEALERMAPPINGLIST")
            		|| isFor.equalsIgnoreCase("DEALERMAPPINGLIST") || isFor.equalsIgnoreCase("EXPORTFERTDASHLIST"))) {
            	lastCol = fieldNames.size()-1;
            }else if(isFor != null && (isFor.equalsIgnoreCase("EXPORTBOMGRP") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST"))) {
            	lastCol = fieldNames.size()-3;
            }else if(isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")) {
            	lastCol = fieldNames.size()-4;
            }
            if(isFor != null && (isFor.equalsIgnoreCase("BOM") || isFor.equalsIgnoreCase("EXPORTREPPOLIST") || isFor.equalsIgnoreCase("EXPORTREPPOLIST"))) {
            	lastCol = fieldNames.size()-3;
            }
            System.out.println("header sheet created");
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
            System.out.println("creating row header");
            Cell cell;
            for (String fieldName : fieldNames) {
            	if(fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("sno"))) {
            		continue;
            	}else if(fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
            		continue;
            	}else if(isFor != null && isFor.equalsIgnoreCase("BOM") &&  fieldName.equals("modelNo")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTBOMGRP") &&  (fieldName.equals("groupNo") || fieldName.equals("groupDesc"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST") &&  (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && fieldName.equals("effDate")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")  && fieldName.equals("effectiveDate")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked") || fieldName.equals("createdBy") 
        				|| fieldName.equals("createdDate"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && fieldName.equals("issueDate")) {
            		continue;
        		}
        		else if(isFor != null && isFor.equalsIgnoreCase("KIT")  && fieldName.equals("issueDate")) {
            		continue;
        		}
                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                sheet.autoSizeColumn(cell.getColumnIndex());
                cell.setCellValue(resetFieldsName(fieldName));
                fieldName = null;
            }
            System.out.println("row header created");
            XSSFCreationHelper createHelper = null;
            XSSFCellStyle cellStyle = null;
            if(isFor != null && (isFor.equalsIgnoreCase("EXPORTREPCHLIST") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST") || isFor.equalsIgnoreCase("BULLETIN"))) {
            	//1. Create the date cell style
            	createHelper = workbook.getCreationHelper();
            	cellStyle = workbook.createCellStyle();
            	cellStyle.setDataFormat(
            	createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
          //      	System.out.println("fieldName : "+fieldName);
                    cell = row.createCell(columnCount);
                    Method method = null;
                    if(fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("modelNo") || fieldName.equals("sno"))) {
                		continue;
                	}else if(fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
                		continue;
                	}else if(isFor != null && isFor.equalsIgnoreCase("BOM") &&  fieldName.equals("modelNo")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTBOMGRP") && (fieldName.equals("groupNo") || fieldName.equals("groupDesc"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST") &&  (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")  && fieldName.equals("effDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")  && fieldName.equals("effectiveDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked") || fieldName.equals("createdBy") 
            				|| fieldName.equals("createdDate"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && fieldName.equals("issueDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("KIT")  && fieldName.equals("issueDate")) {
                		continue;
            		}
                    try {
                        method = classz.getMethod("get" + capitalize(fieldName));
                    } catch (NoSuchMethodException nme) {
                    	isCreated = "NTCREATED";
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(t, (Object[]) null);
        //            System.out.println("value : "+value);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        }else {
                        	cell.setCellValue(""+ value); // Deepak managal Sir 
                        }
                        if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && (fieldName.equals("effectiveDate") || fieldName.equals("EFFECTIVE DATE")) && cellStyle != null) {
                        	cell.setCellStyle(cellStyle);
                        }
                        else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && fieldName.equals("effDate")) {
                        	cell.setCellStyle(cellStyle);
                        }
                        else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && (fieldName.equals("issueDateValue") || fieldName.equals("ISSUE DATE"))) {
                        	cell.setCellStyle(cellStyle);
                        }
                    }
//                    sheet.autoSizeColumn(cell.getColumnIndex());
//                    System.out.println(value+" : columnCount : "+columnCount+" : rowCount : "+rowCount);
                    columnCount++;
                    fieldName = null;
                }
            }
            System.out.println("excel created");
            fos = new FileOutputStream(file);
            System.out.println("file writing in WorkBook..");
            workbook.write(fos);
            fos.flush();
            System.out.println("excel created successfully");
        } catch (Exception e) {
        	isCreated = "NTCREATED";
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
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
            directory = null;
            sheet = null;
            workbook = null;
            style = null;
            fieldNames = null;
            font = null;
        }
        System.out.println("isCreated : "+isCreated);
        return isCreated;
    }
    
    public static <T> String writeToExcelInSheets(final String filePath, String fileName, final String sheetName, final String fertCode, String header,
            final List<T> data, String isFor) {
        File file = null;
        OutputStream fos = null;
        SXSSFWorkbook workbook = null;
        String isCreated = "CREATED";
        System.out.println(fertCode+" : "+filePath+" : fileName : "+fileName);
        File directory = null;
        Sheet sheet = null;
        CellStyle style = null;
        List<String> fieldNames = null;
        Font font = null;
        try {
        	directory = new File(filePath);
        	if (! directory.exists()){
                directory.mkdirs();
            }
        	fileName = filePath+"\\"+fileName;
            file = new File(fileName);
            System.out.println("fileName : : : "+fileName);
            if (file.exists()) {
                workbook = (SXSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
            } else {
                workbook = new SXSSFWorkbook(100);
            }
            int sheetIndex = workbook.getSheetIndex(sheetName);
            if(sheetIndex == -1){
                sheet = workbook.createSheet(sheetName);
            }else {
            	workbook.removeSheetAt(sheetIndex);
            	 sheet = workbook.createSheet(sheetName);
            }
            System.out.println("sheet created");
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
            System.out.println("style sheet created");
            fieldNames = getFieldNamesForClass(data.get(0).getClass());
            int rowCount = 0;
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            int lastCol = fieldNames.size()-2;
            Cell headerCell = row.createCell(columnCount);
            if(header != null || fertCode != null) {
            	headerCell.setCellValue(header+""+(fertCode == null ? "" : fertCode)); //new XSSFRichTextString(fertCode)
            }
            headerCell.setCellStyle(style);
            if(isFor != null && (isFor.equalsIgnoreCase("EXPORTCART")|| isFor.equalsIgnoreCase("EXPORTREPPARTLIST") || isFor.equalsIgnoreCase("EXPORTREPFERTLIST")
            		|| isFor.equalsIgnoreCase("EXPORTREPVINLIST")|| isFor.equalsIgnoreCase("EXPORTREPOBSLIST") || isFor.equalsIgnoreCase("DEALERMAPPINGLIST")
            		|| isFor.equalsIgnoreCase("DEALERMAPPINGLIST") || isFor.equalsIgnoreCase("EXPORTFERTDASHLIST"))) {
            	lastCol = fieldNames.size()-1;
            }else if(isFor != null && (isFor.equalsIgnoreCase("EXPORTBOMGRP") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST"))) {
            	lastCol = fieldNames.size()-3;
            }else if(isFor.equalsIgnoreCase("EXPORTMODELBOMLIST")) {
            	lastCol = fieldNames.size()-4;
            }
            if(isFor != null && (isFor.equalsIgnoreCase("BOM") || isFor.equalsIgnoreCase("EXPORTREPPOLIST") || isFor.equalsIgnoreCase("EXPORTREPPOLIST"))) {
            	lastCol = fieldNames.size()-3;
            }
            System.out.println("header sheet created");
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
            System.out.println("creating row header");
            Cell cell;
            for (String fieldName : fieldNames) {
            	if(fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("sno"))) {
            		continue;
            	}else if(fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
            		continue;
            	}else if(isFor != null && isFor.equalsIgnoreCase("BOM") &&  fieldName.equals("modelNo")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTBOMGRP") &&  (fieldName.equals("groupNo") || fieldName.equals("groupDesc"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST") &&  (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && fieldName.equals("effDate")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")  && fieldName.equals("effectiveDate")) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked") || fieldName.equals("createdBy") 
        				|| fieldName.equals("createdDate"))) {
            		continue;
        		}else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && fieldName.equals("issueDate")) {
            		continue;
        		}
        		else if(isFor != null && isFor.equalsIgnoreCase("KIT")  && fieldName.equals("issueDate")) {
            		continue;
        		}
                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                sheet.autoSizeColumn(cell.getColumnIndex());
                cell.setCellValue(resetFieldsName(fieldName));
                fieldName = null;
            }
            System.out.println("row header created");
            CreationHelper createHelper = null;
            CellStyle cellStyle = null;
            if(isFor != null && (isFor.equalsIgnoreCase("EXPORTREPCHLIST") || isFor.equalsIgnoreCase("EXPORTECNMBOMLIST") || isFor.equalsIgnoreCase("BULLETIN"))) {
            	//1. Create the date cell style
            	createHelper = workbook.getCreationHelper();
            	cellStyle = workbook.createCellStyle();
            	cellStyle.setDataFormat(
            	createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
          //      	System.out.println("fieldName : "+fieldName);
                    cell = row.createCell(columnCount);
                    Method method = null;
                    if(fieldName != null && (fieldName.equals("srNo") || fieldName.equals("srlNo") || fieldName.equals("modelNo") || fieldName.equals("sno"))) {
                		continue;
                	}else if(fieldName != null && (fieldName.equals("lineColor") || fieldName.equals("lineColor"))) {
                		continue;
                	}else if(isFor != null && isFor.equalsIgnoreCase("BOM") &&  fieldName.equals("modelNo")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTBOMGRP") && (fieldName.equals("groupNo") || fieldName.equals("groupDesc"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTMODELBOMLIST") &&  (fieldName.equals("indexNo") || fieldName.equals("actualGroupNo"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTFERTSLIST")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST")  && fieldName.equals("effDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHNGHISLIST")  && fieldName.equals("effectiveDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && (fieldName.equals("ecndataStg_ID") || fieldName.equals("isChecked") || fieldName.equals("createdBy") 
            				|| fieldName.equals("createdDate"))) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && fieldName.equals("issueDate")) {
                		continue;
            		}else if(isFor != null && isFor.equalsIgnoreCase("KIT")  && fieldName.equals("issueDate")) {
                		continue;
            		}
                    try {
                        method = classz.getMethod("get" + capitalize(fieldName));
                    } catch (NoSuchMethodException nme) {
                    	isCreated = "NTCREATED";
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(t, (Object[]) null);
        //            System.out.println("value : "+value);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        }else {
                        	cell.setCellValue(""+ value); // Deepak managal Sir 
                        }
                        if(isFor != null && isFor.equalsIgnoreCase("EXPORTREPCHLIST") && (fieldName.equals("effectiveDate") || fieldName.equals("EFFECTIVE DATE")) && cellStyle != null) {
                        	cell.setCellStyle(cellStyle);
                        }
                        else if(isFor != null && isFor.equalsIgnoreCase("EXPORTECNMBOMLIST")  && fieldName.equals("effDate")) {
                        	cell.setCellStyle(cellStyle);
                        }
                        else if(isFor != null && isFor.equalsIgnoreCase("BULLETIN")  && (fieldName.equals("issueDateValue") || fieldName.equals("ISSUE DATE"))) {
                        	cell.setCellStyle(cellStyle);
                        }
                    }
//                    sheet.autoSizeColumn(cell.getColumnIndex());
//                    System.out.println(value+" : columnCount : "+columnCount+" : rowCount : "+rowCount);
                    columnCount++;
                    fieldName = null;
                }
            }
            System.out.println("excel created");
            fos = new FileOutputStream(file);
            System.out.println("file writing in WorkBook..");
            workbook.write(fos);
            fos.flush();
            System.out.println("excel created successfully");
        } catch (Exception e) {
        	isCreated = "NTCREATED";
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
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
            directory = null;
            sheet = null;
            workbook = null;
            style = null;
            fieldNames = null;
            font = null;
        }
        System.out.println("isCreated : "+isCreated);
        return isCreated;
    }
    
    //retrieve field names from a POJO class
    private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
        }
        return fieldNames;
    }
 
    //capitalize the first letter of the field name for retriving value of the field later
    private static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    private static String resetFieldsName(String fieldName) {
    	if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ASSEMBLYNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ASSEMBLYNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.G_ALT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.G_ALT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.GROUPDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.GROUPDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BASE_FERT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BASE_FERT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.LEVEL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.LEVEL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MAPNAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MAPNAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MATERIALREV.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MATERIALREV.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MODELNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MODELNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MPART_REPLACED_FOR_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MPART_REPLACED_FOR_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OBJECTDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OBJECTDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OBJECTID.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OBJECTID.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PARTDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PARTDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PARTNAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PARTNAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PARTNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PARTNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.QTY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.QTY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.QUANTITY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.QUANTITY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REMARKS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REMARKS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REVISION.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REVISION.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SEQUENCE_POS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SEQUENCE_POS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SERVICEABILITY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SERVICEABILITY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SRLNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SRLNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.AGGREGATEDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.AGGREGATEDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.COMPONENT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.COMPONENT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.QTYVALUE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.QTYVALUE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.GROUPNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.GROUPNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.TOPICNAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.TOPICNAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SUBTOPICNAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SUBTOPICNAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FBDATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FBDATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FBDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FBDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FBGROUP.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FBGROUP.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FBLOGGEDBY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FBLOGGEDBY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.USERNAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.USERNAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CITY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CITY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.EMAIL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.EMAIL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MOBILENO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MOBILENO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REFNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REFNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_NAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.USER_NAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PARTTYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PARTTYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PARTREMARK.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PARTREMARK.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MRP.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MRP.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MRPAVAILABLE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MRPAVAILABLE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERTNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERTNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERTDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERTDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CATALOG_AVAILABLE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CATALOG_AVAILABLE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEWFERT_AVAILABLE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEWFERT_AVAILABLE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.VEHICLE_AVAILABLE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.VEHICLE_AVAILABLE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.VEHICLE_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.VEHICLE_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SO_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SO_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SERIAL_ID.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SERIAL_ID.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DRIVERLINENO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DRIVERLINENO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MFGDATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MFGDATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BUCKLE_UP_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BUCKLE_UP_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PART_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OLD_PART_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PART_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OLD_PART_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PART_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEW_PART_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PART_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEW_PART_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OBS_PART_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OBS_PART_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OBS_PART_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OBS_PART_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.VALID_FROM.getPropertyName())) {
    		fieldName = ModalPropertyEnum.VALID_FROM.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OBS_REMARKS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OBS_REMARKS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CL_CODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CL_CODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CL_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CL_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CL_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CL_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PART.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OLD_PART.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_PARTDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OLD_PARTDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.OLD_QTY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.OLD_QTY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PART.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEW_PART.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_PARTDESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEW_PARTDESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.NEW_QTY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.NEW_QTY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.EFF_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.EFF_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CH_TYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CH_TYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CH_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CH_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CHANGE_PART.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CHANGE_PART.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CHANGE_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CHANGE_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MATNR.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MATNR.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.POS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.POS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERT_AVAILABLE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERT_AVAILABLE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_ID.getPropertyName())) {
    		fieldName = ModalPropertyEnum.USER_ID.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DESCRIPTION.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DESCRIPTION.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ADDRESS1.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ADDRESS1.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ADDRESS2.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ADDRESS2.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.STATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.STATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.COUNTRY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.COUNTRY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PINCODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PINCODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CONTACT_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CONTACT_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MOBILE_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MOBILE_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REG_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REG_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.STATUS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.STATUS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.USER_FULL_NAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.USER_FULL_NAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.LAST_CHANGED.getPropertyName())) {
    		fieldName = ModalPropertyEnum.LAST_CHANGED.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.STLNR.getPropertyName())) {
    		fieldName = ModalPropertyEnum.STLNR.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.WERK_PLANT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.WERK_PLANT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MATR.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MATR.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MAT_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MAT_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REV_LVL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REV_LVL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.GROUP_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.GROUP_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.GROUP_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.GROUP_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.LEV.getPropertyName())) {
    		fieldName = ModalPropertyEnum.LEV.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Object_ID.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Object_ID.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ObjectDescription.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ObjectDescription.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.RevLev.getPropertyName())) {
    		fieldName = ModalPropertyEnum.RevLev.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Quantity.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Quantity.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BUn.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BUn.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MTyp.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MTyp.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ChangedOn.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ChangedOn.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CreatedOn.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CreatedOn.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ValidFrom.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ValidFrom.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Pr.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Pr.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BuMat_IM.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BuMat_IM.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.By1.getPropertyName())) {
    		fieldName = ModalPropertyEnum.By1.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.By2.getPropertyName())) {
    		fieldName = ModalPropertyEnum.By2.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.AltItm.getPropertyName())) {
    		fieldName = ModalPropertyEnum.AltItm.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ChangeNo.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ChangeNo.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PO_AVAIL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PO_AVAIL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PRICE_AVL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PRICE_AVL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PO_AVL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PO_AVL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.HIGHER_LEVEL_ASSY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.HIGHER_LEVEL_ASSY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_PUBLISHED.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PART_PUBLISHED.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PART_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_DESC.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PART_DESC.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PRICE_REQ_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PRICE_REQ_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SLA.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SLA.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PENDING_SINCE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PENDING_SINCE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.RESPONSIBILITY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.RESPONSIBILITY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PO_STATUS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PO_STATUS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PO_REQ_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PO_REQ_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DONE_UNDERSLA_COUNT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DONE_UNDERSLA_COUNT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DONE_SLABREACHED_COUNT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DONE_SLABREACHED_COUNT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PART_CATEGORY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PART_CATEGORY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FROM_USER_CODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FROM_USER_CODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.TO_USER_CODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.TO_USER_CODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.TO_USER_NAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.TO_USER_NAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.HAlB_NONHALB.getPropertyName())) {
    		fieldName = ModalPropertyEnum.HAlB_NONHALB.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DISPLAY_GRPNO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DISPLAY_GRPNO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PLATFORM_NAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PLATFORM_NAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MODEL_NAME.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MODEL_NAME.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.UPLOADED_BY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.UPLOADED_BY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PLANT_CODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PLANT_CODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERT_CODE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERT_CODE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MBOM_FECTH_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MBOM_FECTH_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SR_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SR_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BOM_LEVEL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BOM_LEVEL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REVLVL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REVLVL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.GRP_ALT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.GRP_ALT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CHANGE_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CHANGE_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PRIORITY_ALT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PRIORITY_ALT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CHANGETYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CHANGETYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.EFFECTIVE_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.EFFECTIVE_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CL_Manufacturing_Date.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CL_Manufacturing_Date.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SBOM_download_date.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SBOM_download_date.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DOMESTIC_EXPORT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DOMESTIC_EXPORT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Material_Description.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Material_Description.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PLATFORM.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PLATFORM.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Series.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Series.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Drive_Type.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Drive_Type.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.wheel_Type.getPropertyName())) {
    		fieldName = ModalPropertyEnum.wheel_Type.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Body_Type.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Body_Type.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.PublishStatus.getPropertyName())) {
    		fieldName = ModalPropertyEnum.PublishStatus.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CHANGE_PART_.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CHANGE_PART_.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CHANGE_NO__.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CHANGE_NO__.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SAP_CHANGE_DESC_.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SAP_CHANGE_DESC_.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MODEL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MODEL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SERIES.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SERIES.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REMARK.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REMARK.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ECN_STATUS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ECN_STATUS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ENGINE_MODEL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ENGINE_MODEL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ENGINE_SERIES.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ENGINE_SERIES.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.lEVEL.getPropertyName())) {
    		fieldName = ModalPropertyEnum.lEVEL.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.APPLICATION_TYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.APPLICATION_TYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BULLETIN_TYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BULLETIN_TYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SUBJECT.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SUBJECT.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ISSUE_DATE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ISSUE_DATE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ISSUE_DATE_STR.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ISSUE_DATE_STR.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.BULLETIN_NUMBER.getPropertyName())) {
    		fieldName = ModalPropertyEnum.BULLETIN_NUMBER.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.DOCUMENT_NUMBER.getPropertyName())) {
    		fieldName = ModalPropertyEnum.DOCUMENT_NUMBER.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.LEVEL_4.getPropertyName())) {
    		fieldName = ModalPropertyEnum.LEVEL_4.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.REG_NO.getPropertyName())) {
    		fieldName = ModalPropertyEnum.REG_NO.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.EMISSION_NORMS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.EMISSION_NORMS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.MARKET.getPropertyName())) {
    		fieldName = ModalPropertyEnum.MARKET.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.EMISSION_FUEL_TYPE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.EMISSION_FUEL_TYPE.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.TotalVinCount.getPropertyName())) {
    		fieldName = ModalPropertyEnum.TotalVinCount.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SBOM_RequestDate.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SBOM_RequestDate.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.ReusedGroupCount.getPropertyName())) {
    		fieldName = ModalPropertyEnum.ReusedGroupCount.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.CatalogueStatus.getPropertyName())) {
    		fieldName = ModalPropertyEnum.CatalogueStatus.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.FERT_DESCRIPTION.getPropertyName())) {
    		fieldName = ModalPropertyEnum.FERT_DESCRIPTION.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.AVAIL_IN_SBOM.getPropertyName())) {
    		fieldName = ModalPropertyEnum.AVAIL_IN_SBOM.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.Part_Publish_Status.getPropertyName())) {
    		fieldName = ModalPropertyEnum.Part_Publish_Status.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.INPUT_PART.getPropertyName())) {
    		fieldName = ModalPropertyEnum.INPUT_PART.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.AUTHOR_REMARKS.getPropertyName())) {
    		fieldName = ModalPropertyEnum.AUTHOR_REMARKS.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.SBOM_SERVICEABILITY.getPropertyName())) {
    		fieldName = ModalPropertyEnum.SBOM_SERVICEABILITY.getDescp();
    	}else if(fieldName.equalsIgnoreCase(ModalPropertyEnum.LEVEL_VALUE.getPropertyName())) {
    		fieldName = ModalPropertyEnum.LEVEL_VALUE.getDescp();
    	}
    	
    	return fieldName;
    }
 
}
