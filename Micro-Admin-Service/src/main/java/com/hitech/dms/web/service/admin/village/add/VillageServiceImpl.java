package com.hitech.dms.web.service.admin.village.add;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.admin.village.add.VillageDao;
import com.hitech.dms.web.model.admin.village.request.VillageRequest;
import com.hitech.dms.web.model.admin.village.response.VillageResponse;
import com.hitech.dms.web.model.admin.village.response.VillageUploadExcelRes;

@Service
public class VillageServiceImpl implements VillageService{
	
	@Autowired
	private VillageDao villageDao;

	@Override
	public VillageResponse addVillage(String authorizationHeader, String userCode, @Valid VillageRequest requestModel,
			Device device) {
		List<VillageRequest> dataList = new ArrayList<VillageRequest>();
		dataList.add(requestModel);
		return villageDao.addVillage(userCode, dataList);
	}

	@SuppressWarnings("resource")
	@Override
	public VillageUploadExcelRes addVillageUploadedFile(String authorizationHeader, String userCode, MultipartFile file) {
	    VillageUploadExcelRes responseModel = new VillageUploadExcelRes();
	    List<VillageRequest> dataList = new ArrayList<VillageRequest>();
	    try {
	        List<String> staticHeaders = Arrays.asList("Country Name", "State Name", "District Name", "Tehsil Name", "City/ Village Name", "Locality Name", "Pincode");
	        Map<Integer, String> columnHeaderMap = new HashMap<>();
	      
	        Map<String, String> errorData = new HashMap<>();

	        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        int totalRows = sheet.getPhysicalNumberOfRows();
	        List<Integer> evenColumns = findEven(totalRows);

	        Row headerRow = sheet.getRow(0);
	        short minColIndex = headerRow.getFirstCellNum();
	        short maxColIndex = headerRow.getLastCellNum();

	        // Validate headers
	        for (short colIndex = minColIndex; colIndex < maxColIndex; colIndex++) {
	            Cell cell = headerRow.getCell(colIndex);
	            if (staticHeaders.contains(cell.getStringCellValue().trim())) {
	                columnHeaderMap.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
	            } else {
	                return invalidFormatResponse(responseModel);
	            }
	        }

	        for (int rowIndex = 1; rowIndex < totalRows; rowIndex++) {
	        	VillageRequest data = null;
	            Row row = sheet.getRow(rowIndex);
	            String country = extractCellValue(row.getCell(0));
	            String state = extractCellValue(row.getCell(1));
	            String district = extractCellValue(row.getCell(2));
	            String tehsil = extractCellValue(row.getCell(3));
	            String village = extractCellValue(row.getCell(4));
	            String locality = extractCellValue(row.getCell(5));
	            String pincode = extractCellValue(row.getCell(6));
	            
	            if (isEmpty(country) && isEmpty(state) && isEmpty(district) && 
	            	    isEmpty(tehsil) && isEmpty(village) && isEmpty(locality) && 
	            	    isEmpty(pincode) && pincode.length()==6) {
	            	    addError(rowIndex, "One or more mandatory fields are missing.", errorData);
//	            	    responseModel.setErrorData(errorData);
//	            	    responseModel.setMsg("Errors in uploaded data.");
//	    	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	            	   // return; 
	            	}else {

			            data = new VillageRequest(country,state,district,tehsil,village,locality,Integer.valueOf(pincode));
			            dataList.add(data);
	            	}
	          
	        }
	        if(!dataList.isEmpty()) {
	        	 villageDao.addVillage(userCode, dataList);
	        }
	        if (errorData.isEmpty()) {
	       //     responseModel.setPartAndQty(data);
	            responseModel.setMsg("Add Village Uploaded Successfully.");
	            responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
	        } else {
	         //   responseModel.setPartAndQty(data);
	          //  responseModel.setErrorPartData(errorData);
	            responseModel.setMsg("Errors in uploaded data.");
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        }
	    } catch (Exception e) {
	        responseModel.setMsg("Error processing file: " + e.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	    }

	    return responseModel;
	}

	private String extractCellValue(Cell cell) {
	    if (cell == null) return "";
	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case NUMERIC:
	            return new DecimalFormat("#").format(cell.getNumericCellValue());
	        case BLANK:
	        default:
	            return "";
	    }
	}

	private void addError(int rowIndex, String message, Map<String, String> errorData) {
	    errorData.put(String.valueOf(rowIndex + 1), message);
	}

	private VillageUploadExcelRes invalidFormatResponse(VillageUploadExcelRes responseModel) {
	    responseModel.setMsg("INVALID EXCEL FORMAT.");
	    responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
	    return responseModel;
	}

	
	/**
	 * @param totalRows
	 * @return
	 */
	private List<Integer> findEven(int totalRows) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= totalRows; i++) {
			if (i % 2 == 0) {
				list.add(i);
			}
		}
		return list;
	}

	private boolean isEmpty(String value) {
	    return value == null || value.trim().isEmpty();
	}
	

}
