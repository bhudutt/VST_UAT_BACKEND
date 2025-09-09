package com.hitech.dms.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hitech.dms.web.model.enquiry.digitalReport.response.DigitalUploadResponseModel;


public class ExcelCellGenerator {
    public static ByteArrayInputStream digitalUploadReport(List<DigitalUploadResponseModel> du) throws IOException{
    	String [] Columns = {"Digital Enquiry No","Digital Source Name","Profit  Center","Model","Customer Name","Customer Mobile No","Customer Email ID",
    			"Customer State","Customer District", "Customer Tehsil","Segment","Status","Error Details"};
    	
		    	try( Workbook workbook = new XSSFWorkbook();
		    			ByteArrayOutputStream out = new ByteArrayOutputStream();
		    		){
		    		 //CreationHelper creationHelper = workbook.getCreationHelper();
		    		 Sheet sheet = workbook.createSheet("Digital Report");
		    		 Font headerFont = workbook.createFont();
		             headerFont.setBold(true);
		             CellStyle headerCellStyle = workbook.createCellStyle();
		             headerCellStyle.setFont(headerFont);
		             Row headerRow = sheet.createRow(0);
		             for (int col = 0; col < Columns.length; col++) {
		                 Cell cell = headerRow.createCell(col);
		                 cell.setCellValue(Columns[col]);
		                 cell.setCellStyle(headerCellStyle);
		             }
		             int rowIdx = 1;
		             for(DigitalUploadResponseModel ex:du){
		            	 Row row = sheet.createRow(rowIdx++);
		            	 row.createCell(0).setCellValue(ex.getDigital_Enq_No());
		            	 row.createCell(1).setCellValue(ex.getDigitalSourceName());
		            	 row.createCell(2).setCellValue(ex.getPc_desc());
		            	 row.createCell(3).setCellValue(ex.getModel());
		            	 row.createCell(4).setCellValue(ex.getCustomer_Name());
		            	 row.createCell(5).setCellValue(ex.getCustomer_Mobile_No());
		            	 row.createCell(6).setCellValue(ex.getCustomer_Email_ID());
		            	 row.createCell(7).setCellValue(ex.getCustomer_State());
		            	 row.createCell(8).setCellValue(ex.getCustomer_District());
		            	 row.createCell(9).setCellValue(ex.getCustomer_Tehsil());
		            	 row.createCell(10).setCellValue(ex.getSegment());
		            	 row.createCell(11).setCellValue(ex.getStatus());
		            	 row.createCell(12).setCellValue(ex.getError_Detail());
		             }
		             workbook.write(out);
		             out.close();
		             return new ByteArrayInputStream(out.toByteArray());
				}
		    }

}
