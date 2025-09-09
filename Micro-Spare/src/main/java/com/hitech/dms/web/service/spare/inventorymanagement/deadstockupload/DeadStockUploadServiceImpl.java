package com.hitech.dms.web.service.spare.inventorymanagement.deadstockupload;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.spare.inventorymanagement.deadstockupload.DeadStockUploadDao;
import com.hitech.dms.web.entity.spare.inventorymanagement.deadstockupload.DeadStockUploadEntity;
import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockSearchRequest;
import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockUploadResponseDto;
import com.hitech.dms.web.service.common.Utils;
import com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer.BinToBinTransferServiceImpl;

/**
 * @author mahesh.kumar
 */
@Service
public class DeadStockUploadServiceImpl implements DeadStockUploadService{
	private static final Logger logger = LoggerFactory.getLogger(BinToBinTransferServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DeadStockUploadDao deadStockUploadDao;
	
	@Override
	public ApiResponse<?> uploadDeadStock(BigInteger branchId,MultipartFile deadStockFile) 
	{		
		Session session = null;
	    ApiResponse<List<DeadStockUploadResponseDto>> apiResponse = null;
	    List<?> data = null;
	    List<DeadStockUploadResponseDto> deadStockListResponses = new ArrayList<>();
	    String isExist = "";

	    try {
	        session = sessionFactory.openSession();
	        
	        XSSFWorkbook workbook = new XSSFWorkbook(deadStockFile.getInputStream());
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        Row headerRow = sheet.getRow(0);
	        int getColumnsNo = headerRow.getLastCellNum();
	        
	        //Not defined no. of columns
	        if(getColumnsNo != 3) {
	        	workbook.close();
	        	apiResponse = new ApiResponse<>();
	        	apiResponse.setMessage("File should have defined number of columns");
	        	apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
	        	throw new Exception("File should have defined number of columns");
	        }
	        
	        //column names and order should be matched with required data.
	        if(headerRow.getCell(0) != null || headerRow.getCell(1) != null || headerRow.getCell(2) != null) {
				
	        	if(!headerRow.getCell(0).getStringCellValue().equalsIgnoreCase("PART NO") || 
	        			!headerRow.getCell(1).getStringCellValue().equalsIgnoreCase("DEAD STOCK QTY") || 
	        			!headerRow.getCell(2).getStringCellValue().equalsIgnoreCase("DATE OF PACKING")) 
	        	{
	        		workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setMessage("Columns names and order should be: PART NO, DEAD STOCK QTY, DATE OF PACKING");
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					throw new IOException("Columns names and order should be: PART NO, DEAD STOCK QTY, DATE OF PACKING");
	        	}
			}
	        
	        int totalRows = sheet.getPhysicalNumberOfRows();
			
	        //doing business requirement
			for(int rowIndx = 1; rowIndx < totalRows; rowIndx++) {
				isExist = "NO";
				
				Row row = sheet.getRow(rowIndx);
				
				//Any cell should not be empty
				if(row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null) {
					
					workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setMessage("Values in cell cann't be empty.");
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					throw new IOException("Values in cell cann't be empty.");
				}
				
				data = deadStockUploadDao.uploadDeadStock(session, branchId,row.getCell(0).getStringCellValue());

				@SuppressWarnings({"rawtypes" })
				Map map = (Map)data.get(0);
				isExist = (String)map.get("partExists");
				
				if(isExist.equalsIgnoreCase("NO")) {
					workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					apiResponse.setMessage("Part No. not found: " + row.getCell(0).getStringCellValue() 
							+ ", kindly put correct details");
					throw new Exception("Part No. not found: " + row.getCell(0).getStringCellValue() 
							+ ", kindly put correct details");
				}
			
				DeadStockUploadResponseDto deadStockResponse = new DeadStockUploadResponseDto();
				deadStockResponse.setPartId((Integer)map.get("partId"));
				deadStockResponse.setPartNo((String)map.get("partNo"));
				deadStockResponse.setPartDesc((String)map.get("partDesc"));
				deadStockResponse.setProdCategoryId((BigInteger)map.get("prodCategoryId"));
				deadStockResponse.setProductCategory((String)map.get("productCategory"));
				deadStockResponse.setProdSubCategoryId((BigInteger)map.get("prodSubCategoryId"));
				deadStockResponse.setProductSubCategory((String)map.get("productSubCategory"));;
				deadStockResponse.setCurrentStock((BigDecimal)map.get("currentStock"));
				deadStockResponse.setDeadStockQty(new BigDecimal(row.getCell(1).getNumericCellValue()));
				
				
				Cell dateCell = row.getCell(2);

				if (dateCell.getCellType() == CellType.NUMERIC) {
				    // If the cell type is numeric, use getDateCellValue directly
				    deadStockResponse.setDateOfPacking(dateCell.getDateCellValue());
				} else if (dateCell.getCellType() == CellType.STRING) {
				    // If the cell type is string, parse the string into a Date
				    String dateString = dateCell.getStringCellValue();
				    // We might need to adjust the date format based on the actual format in the cell
				    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				    try {
				        Date parsedDate = dateFormat.parse(dateString);
				        deadStockResponse.setDateOfPacking(parsedDate);
				    } catch (ParseException e) {
				        e.printStackTrace(); // Handle the parse exception as needed
				    }
				} else {
				    // Handle other cell types if necessary
				}

			
				
//				deadStockResponse.setDateOfPacking(row.getCell(2).getDateCellValue());
				deadStockResponse.setMrp((BigDecimal)map.get("mrp"));
				
				deadStockListResponses.add(deadStockResponse);
			}
	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch(IOException exp) {
	    	String errorMessage = "An IO Exception occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } finally {
	    	if (isExist.equals("YES") && data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(deadStockListResponses);
	    		apiResponse.setMessage("Get Dead Stock Successful!");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> saveDeadStockUpload(String userCode, List<DeadStockUploadEntity> requestModels, List<MultipartFile> files) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveDeadStock invoked.." + userCode);
			logger.debug(requestModels.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<List<DeadStockUploadEntity>> response = null;
		boolean isSuccess = false;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

//			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModels != null && !requestModels.isEmpty()) {
				
				for(DeadStockUploadEntity requestModel : requestModels) {
					if(requestModel.getId() == null) {
						requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
						requestModel.setCreatedDate(new Date());
						requestModel.setUserCode(userCode);
			        }
					else {
						requestModel.setModifiedBy((BigInteger)mapData.get("userId"));
						requestModel.setModifiedDate(new Date());
			        }
					
					//Saving photo
			        if (files.size() <= 5 && !files.isEmpty()) {
			        	for(MultipartFile file: files){
//			        		DeadStockUploadEntity deadPhotos = new DeadStockUploadEntity();
			                String originalPhoto = file.getOriginalFilename();
			                String photoName = "DeadStock" + System.currentTimeMillis() + "_"+originalPhoto;
			                utils.store(file, "dead-stock", photoName);
			                requestModel.setFileContentType(file.getContentType());
			                requestModel.setFileName(photoName);
//			                warrantyPcrPhotos.setServiceWarrantyPcr(requestModel);
			                
//			                session.save(requestModel);
			            }
			        }
					
					session.save(requestModel);
				}
				isSuccess = true;
		        
			} 
			
			//Committing the transaction if everything went well.
			if (isSuccess) {
				transaction.commit();
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) transaction.rollback();
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) transaction.rollback();
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) transaction.rollback();
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (isSuccess) {
				if (requestModels != null) {
					response = new ApiResponse<>();
					response.setResult(requestModels);
					response.setMessage("Dead Stock Saved Successfully.");
					response.setStatus(WebConstants.STATUS_CREATED_201);
				}
			}
			if (session != null) {
				session.close();
			}
		}
		return response;
	}
	
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoCompletePartNo(String partNo, BigInteger branchId) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = deadStockUploadDao.autoCompletePartNo(session, partNo, branchId);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("partId", row.get("partId"));
	                responseModel.put("partNo", row.get("partNo"));
	                responseModelList.add(responseModel);
	            }

	            apiResponse.setResult(responseModelList); // Set the result in the ApiResponse
	        } else {
	            apiResponse.setMessage("No data found."); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> searchDeadStockUpload(String userCode, DeadStockSearchRequest requestModel) {		
	    Session session = null;
	    ApiResponse<List<DeadStockUploadResponseDto>> apiResponse = null;
	    List<?> data = null;
	    List<DeadStockUploadResponseDto> deadStockListResponses = new ArrayList<>();
	    Integer dataCount = 0;

	    try {
	        session = sessionFactory.openSession();
	        
	        data = deadStockUploadDao.searchDeadStockUpload(session, userCode, requestModel);
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings({"rawtypes" })
	                Map map = (Map)object;  // Fix: Cast the current object to a Map
	                
	                DeadStockUploadResponseDto deadStockResponse = new DeadStockUploadResponseDto();
	                deadStockResponse.setDealerCode((String)map.get("dealerCode"));
	                deadStockResponse.setDealerName((String)map.get("dealerName"));
	                deadStockResponse.setDealerLocation((String)map.get("dealerLocation"));
	                deadStockResponse.setPartId((Integer)map.get("partId"));
	                deadStockResponse.setPartNo((String)map.get("partNo"));
	                deadStockResponse.setPartDesc((String)map.get("partDesc"));
	                deadStockResponse.setProdCategoryId((BigInteger)map.get("prodCategoryId"));
	                deadStockResponse.setProductCategory((String)map.get("productCategory"));
	                deadStockResponse.setProdSubCategoryId((BigInteger)map.get("prodSubCategoryId"));
	                deadStockResponse.setProductSubCategory((String)map.get("productSubCategory"));;
	                deadStockResponse.setCurrentStock((BigDecimal)map.get("currentStock"));
	                deadStockResponse.setDeadStockQty((BigDecimal)map.get("deadStkQty"));
	                deadStockResponse.setDateOfPacking((Date)map.get("pakingDate"));
	                deadStockResponse.setMrp((BigDecimal)map.get("mrp"));
	                deadStockResponse.setFileType((String)map.get("fileType"));
	                deadStockResponse.setFileName((String)map.get("fileName"));
	                dataCount = (Integer) map.get("recordCount");
	                
	                deadStockListResponses.add(deadStockResponse);
	            }
	        }
	    } catch (SQLGrammarException exp) {
	        String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	        String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } finally {
	        if (data != null) {
	            apiResponse = new ApiResponse<>();
	            apiResponse.setCount(dataCount);
	            apiResponse.setResult(deadStockListResponses);
	            apiResponse.setMessage("Get Dead Stock Successful!");
	            apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
	        }
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}


}
