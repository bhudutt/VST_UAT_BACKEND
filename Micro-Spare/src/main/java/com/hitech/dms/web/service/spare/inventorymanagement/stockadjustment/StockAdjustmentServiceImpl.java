package com.hitech.dms.web.service.spare.inventorymanagement.stockadjustment;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import com.hitech.dms.web.dao.spare.inventorymanagement.bintobintransfer.BinToBinTransferDao;
import com.hitech.dms.web.dao.spare.inventorymanagement.stockadjustment.StockAdjustmentDao;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentApproval;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentDtl;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentDtlTemp;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentListResponse;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentResponse;
import com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer.BinToBinTransferServiceImpl;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.SearchStockAdjustmentRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.SearchStockAdjustmentResponseDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentApprovalRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentApprovalResponseDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentDetailDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentViewDto;

@Service
public class StockAdjustmentServiceImpl implements StockAdjustmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(BinToBinTransferServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private StockAdjustmentDao adjustmentDao;
	
	@Autowired
	private BinToBinTransferDao binTransferDao;

	@Override
	public ApiResponse<?> uploadStockAdjustment(BigInteger branchId, MultipartFile stockAdjfile) 
	{		
		Session session = null;
	    ApiResponse<StockAdjustmentResponse> apiResponse = null;
	    List<?> data = null;
	    
	    List<StockAdjustmentListResponse> adjustmentListResponses = new ArrayList<>();
	    StockAdjustmentResponse finalList = new StockAdjustmentResponse();
	    String isExist = "";
	    Map<Integer, String> errorData = new HashMap<>();
	    try {
	        session = sessionFactory.openSession();
	        
	        XSSFWorkbook workbook = new XSSFWorkbook(stockAdjfile.getInputStream());
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        Row headerRow = sheet.getRow(0);
	        int getColumnsNo = headerRow.getLastCellNum();
	        
	        //Not defined no. of columns
	        if(getColumnsNo != 7) {
	        	workbook.close();
	        	apiResponse = new ApiResponse<>();
	        	apiResponse.setMessage("File should have defined number of columns");
	        	apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
	        	throw new Exception("File should have defined number of columns");
	        }
	        
	        //column names and order should be matched with required data.
	        if(headerRow.getCell(0) != null || headerRow.getCell(1) != null || headerRow.getCell(2) != null || 
	        		headerRow.getCell(3) != null || headerRow.getCell(4) != null || headerRow.getCell(5) != null
	        		|| headerRow.getCell(6) != null) {
				
	        	if(!headerRow.getCell(0).getStringCellValue().equalsIgnoreCase("PART NO") || 
	        			!headerRow.getCell(1).getStringCellValue().equalsIgnoreCase("STORE") || 
	        			!headerRow.getCell(2).getStringCellValue().equalsIgnoreCase("STORE BIN LOCATION") ||  
	        			!headerRow.getCell(3).getStringCellValue().equalsIgnoreCase("ADJUSTMENT TYPE") ||
	        			!headerRow.getCell(4).getStringCellValue().equalsIgnoreCase("QUANTITY") ||
	        			!headerRow.getCell(5).getStringCellValue().equalsIgnoreCase("MRP") ||
	        			!headerRow.getCell(6).getStringCellValue().equalsIgnoreCase("REASON")) 
	        	{
	        		workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setMessage("Columns names and order should be: PART NO, STORE, STORE BIN LOCATION, "
							+ "ADJUSTMENT TYPE, QUANTITY, MRP, REASON");
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					throw new IOException("Columns names and order should be: PART NO, STORE, STORE BIN LOCATION, "
							+ "ADJUSTMENT TYPE, QUANTITY, MRP, REASON");
	        	}
			}
	        
	        int totalRows = sheet.getPhysicalNumberOfRows();
	        //Checking duplicate part numbers inserted in the excel
	        Set<String> set = new HashSet<>();
	        for(int rowIndx = 1; rowIndx < totalRows; rowIndx++) {
	        	Row row = sheet.getRow(rowIndx);
	        	
	        	if(row.getCell(0) != null) {
	        		Cell cell = row.getCell(0);
	        	    String partNo = "";
	        	    if (cell.getCellType() == CellType.STRING) {
	        	    	partNo = cell.getStringCellValue();
	        	    } else if (cell.getCellType() == CellType.NUMERIC) {
	        	    	partNo = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString(); // Convert numeric to string
	        	    }
	        	    if(!set.contains(partNo)) {
	        	        set.add(partNo);
	        	    }
	        		else {
	        			workbook.close();
						apiResponse = new ApiResponse<>();
						errorData.put(rowIndx,"Parts should not be duplicate - "+partNo);
						apiResponse.setMessage("Parts should not be duplicate!");
						apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
						finalList.setErrorPartData(errorData);
						apiResponse.setResult(finalList);
					//	throw new Exception("Parts should not be duplicate!");
					}
	        	}
	        }
			
	        //doing business requirement
			for(int rowIndx = 1; rowIndx < totalRows; rowIndx++) {
				isExist = "NO";
				
				Row row = sheet.getRow(rowIndx);
				
				Cell cell = row.getCell(0);
        	    String partNo = "";
        	    if (cell.getCellType() == CellType.STRING) {
        	    	partNo = cell.getStringCellValue();
        	    } else if (cell.getCellType() == CellType.NUMERIC) {
//        	    	partNo = String.valueOf((Long)cell.getNumericCellValue()); // Convert numeric to string
        	    	BigInteger bigValue = BigInteger.valueOf((long) cell.getNumericCellValue());
        	        partNo = bigValue.toString();
        	    }
				
				//Any cell should not be empty
				if(row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null || 
						row.getCell(3) == null || row.getCell(4) == null || row.getCell(5) == null) {
					
					workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setMessage("Values in cell cann't be empty except column: Reason.");
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					throw new IOException("Values in cell cann't be empty.");
				}
				Cell bincell = row.getCell(2);
				 String binLocationName = "";
				if(bincell.getCellType() == CellType.NUMERIC) {
					
					BigInteger bigValue = BigInteger.valueOf((long)bincell.getNumericCellValue());
					binLocationName = bigValue.toString();
				}else {
					binLocationName = row.getCell(2).getStringCellValue();
				}
				
				data = adjustmentDao.checkIfPartOrMrpExist(session, branchId, row.getCell(1).getStringCellValue(), 
						binLocationName, partNo,
						new BigDecimal(row.getCell(5).getNumericCellValue()), row.getCell(3).getStringCellValue());
				
				//If MRP not found for item entered
				@SuppressWarnings({"rawtypes" })
				Map map = (Map)data.get(0);
				isExist = (String)map.get("partExists");
				StockAdjustmentListResponse adjustmentListResponse = new StockAdjustmentListResponse();
				if(isExist.equalsIgnoreCase("NO")) {
					workbook.close();
					apiResponse = new ApiResponse<>();
					apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
					
					if(row.getCell(3).getStringCellValue().equalsIgnoreCase("INCREASE")) {
						
						errorData.put(rowIndx, " - "+partNo);
//						apiResponse.setMessage("Part No. not found: " + partNo 
//								+ ", kindly put correct details");
					//	throw new Exception("Part No. not found: " + partNo +", kindly put correct details");
						
						
						//""
						
						
					}
					else if(row.getCell(3).getStringCellValue().equalsIgnoreCase("DECREASE")) {
						
						errorData.put(rowIndx, "Part MRP not found for part no: " + partNo + 
								", and MRP: " + row.getCell(5).getNumericCellValue() + ", kindly put correct details");
						
					//	apiResponse.setMessage();
					//	throw new Exception("Part MRP not found for part no: " + partNo + ", and MRP: " + row.getCell(5).getNumericCellValue() + ", kindly put correct details");
					}
				}else {
				
				
				adjustmentListResponse.setPartBarnchId((BigInteger)map.get("partBranchId"));
				//added partId on 29-05-2024 by mahesh.kumar
				adjustmentListResponse.setPartId((BigInteger)map.get("partId"));
				adjustmentListResponse.setPartNo((String)map.get("partNo"));
				adjustmentListResponse.setPartDesc((String)map.get("partDesc"));
				adjustmentListResponse.setProdCategoryId((BigInteger)map.get("prodCategoryId"));
				adjustmentListResponse.setProductCategory((String)map.get("productCategory"));
				adjustmentListResponse.setProdSubCategoryId((BigInteger)map.get("prodSubCategoryId"));
				adjustmentListResponse.setProductSubCategory((String)map.get("productSubCategory"));
				adjustmentListResponse.setStockStoreId((BigInteger)map.get("stockStoreId"));
				//added storeId on 29-05-2024 by mahesh.kumar
				adjustmentListResponse.setStoreId((BigInteger)map.get("storeId"));
				adjustmentListResponse.setStore((String)map.get("store"));
				adjustmentListResponse.setBinLocationId((BigInteger)map.get("binLocationId"));
				adjustmentListResponse.setBinLocation((String)map.get("binLocation"));
				adjustmentListResponse.setSystemStock((BigDecimal)map.get("systemStock"));
				adjustmentListResponse.setAdjustmentType(row.getCell(3).getStringCellValue());
				adjustmentListResponse.setAdjustmentQty(new BigDecimal(row.getCell(4).getNumericCellValue()));
				adjustmentListResponse.setMrp((BigDecimal)map.get("mrp"));
				adjustmentListResponse.setNdp((BigDecimal)map.get("ndp"));
				adjustmentListResponse.setValue(adjustmentListResponse.getNdp().multiply(adjustmentListResponse.getAdjustmentQty()));
				adjustmentListResponse.setReason(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "");
				
				}
				finalList.setErrorPartData(errorData);
				adjustmentListResponses.add(adjustmentListResponse);
				finalList.setAdjustmentListResponses(adjustmentListResponses);
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
	    	if (isExist!=null && !isExist.isEmpty()) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(finalList);
	    		apiResponse.setMessage("Get Stock Adjustment Successful!");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> saveStockAdjustment(String userCode, SpareStockAdjustmentHdr requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveStockAdjustment invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<SpareStockAdjustmentHdr> response = null;
		String adjustmentNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				adjustmentNo = commonDao.getDocumentNumberById("SA", branchId, session);
				commonDao.updateDocumentNumber("Stock Adjustment", branchId, adjustmentNo, session);
				
				requestModel.setAdjustmentDate(new Date());
				requestModel.setAdjustmentNo(adjustmentNo);
				requestModel.setStatus("Waiting for Approval");
				
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedDate(new Date());
		        }
				else {
					requestModel.setModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setModifiedDate(new Date());
		        }
				
				// Determine which list to use based on partBranchId
				List<SpareStockAdjustmentDtl> dtlsToSave = new ArrayList<>();
				List<SpareStockAdjustmentDtlTemp> dtlsToSaveTemp = new ArrayList<>();

				boolean anyNullPartBranchId = false;
				for (SpareStockAdjustmentDtl temp : requestModel.getAdjustmentDtls()) {
				    if (temp.getPartBranchId() == null) {
				        anyNullPartBranchId = true;
				        break;
				    }
				}

				if (anyNullPartBranchId) {
				    // If any partBranchId is null, save into adjustmentDtlsTemp
				        dtlsToSaveTemp.addAll(requestModel.getAdjustmentDtlsTemp());
				        requestModel.setAdjustmentDtlsTemp(dtlsToSaveTemp);
//				        requestModel.setAdjustmentDtls(null);
//				    }
				} else {
				    // Otherwise, save into adjustmentDtls
				    dtlsToSave.addAll(requestModel.getAdjustmentDtls());
				    requestModel.setAdjustmentDtls(dtlsToSave);
				    requestModel.setAdjustmentDtlsTemp(null);
				}

				
		        session.save(requestModel);
		        
		        //Setting hierarchy in table
		        List<SpareStockAdjustmentApproval> adjustmentApprovals = new ArrayList<>();
				List<?> data = adjustmentDao.getApprovalHierarchy(session, requestModel.isDealerFlag());
				
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						@SuppressWarnings("rawtypes")
						Map row = (Map) object;
						
						SpareStockAdjustmentApproval hierarchyData = new SpareStockAdjustmentApproval();
						hierarchyData.setAdjustmentId(requestModel.getId());
						hierarchyData.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
						hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
						hierarchyData.setGrpSeqNo((Integer) row.get("grp_seq_no"));
						hierarchyData.setApprovalStatus((String) row.get("approvalStatus"));
						hierarchyData.setIsfinalapprovalstatus((Character) row.get("isFinalApprovalStatus"));
						hierarchyData.setRejectedFlag('N');
						hierarchyData.setDealerFlag(requestModel.isDealerFlag());

						adjustmentApprovals.add(hierarchyData);
					}
				}
		        if (!adjustmentApprovals.isEmpty()) {
		            for (SpareStockAdjustmentApproval approvalEntity : adjustmentApprovals) {
		                session.save(approvalEntity);
		            }
		        }
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
				if (requestModel != null) {
					response = new ApiResponse<>();
					response.setResult(requestModel);
					response.setMessage("Stock Adjustment Created Successfully.");
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
	public ApiResponse<?> autoSearchAdjustmentNo(String adjustmentNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchAdjustmentNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = adjustmentDao.autoSearchAdjustmentNo(session, adjustmentNo);

//	        apiResponse.setResult(data);

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
	    		apiResponse.setResult(data);
	    		apiResponse.setMessage("Auto Search Adjustment No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<List<SearchStockAdjustmentResponseDto>> searchStockAdjustment(String userCode, SearchStockAdjustmentRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<SearchStockAdjustmentResponseDto>> apiResponse = null;
	    List<SearchStockAdjustmentResponseDto> responseList = null;
	    List<?> data = null;
	    Integer dataCount = 0;


	    try {
	        session = sessionFactory.openSession();

	        data = adjustmentDao.searchStockAdjustment(session, userCode, requestModel);

	        responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                SearchStockAdjustmentResponseDto response = new SearchStockAdjustmentResponseDto();
	                response.setId((BigInteger)row.get("id"));
	                response.setBranchName((String)row.get("BranchName"));                
	                response.setAction((String)row.get("Action"));
	                response.setAdjustmentNo((String) row.get("adjustmentNo"));
	                response.setAdjustmentDate((Date) row.get("adjustmentDate"));
	                response.setStatus((String) row.get("status"));
	                response.setRemarks((String) row.get("remarks"));
	                response.setAdjustmentDoneBy((String) row.get("adjustmentDoneBy"));
	                response.setDealerFlag((Boolean)row.get("dealerFlag"));
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
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
	    		apiResponse.setResult(responseList);
	    		apiResponse.setMessage("Stock Adjustment Search Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<?> viewStockAdjustment(BigInteger adjustmentId) {
		Session session = null;
	    ApiResponse<StockAdjustmentViewDto> apiResponse = null;
	    StockAdjustmentViewDto responseDto = new StockAdjustmentViewDto();
	    List<StockAdjustmentDetailDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = adjustmentDao.viewStockAdjustment(session, adjustmentId, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setId((BigInteger)row.get("id"));
	        	responseDto.setBranchId((BigInteger)row.get("branchId"));
	        	responseDto.setBranchName((String)row.get("branchName"));
	        	responseDto.setAdjustmentNo((String)row.get("adjustmentNo"));
	        	responseDto.setAdjustmentDate((Date)row.get("adjustmentDate"));
				responseDto.setStatus((String)row.get("status"));
				responseDto.setRemarks((String)row.get("remarks"));
				responseDto.setAdjustmentDoneBy((String)row.get("adjustmentDoneBy"));
				responseDto.setTotatIncreseQty((BigDecimal)row.get("totalIncreaseQty"));
				responseDto.setTotatIncreseValue((BigDecimal)row.get("totalIncreaseValue"));
				responseDto.setTotatDecreaseQty((BigDecimal)row.get("totalDecreaseQty"));
				responseDto.setTotatDecreaseValue((BigDecimal)row.get("totalDecreaseValue"));
	        }
	        
	        //for getting Detail Data
	        data = null;
	        data = adjustmentDao.viewStockAdjustment(session, adjustmentId, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					StockAdjustmentDetailDto dtlResponseDto = new StockAdjustmentDetailDto();
					dtlResponseDto.setAdjustmentId((BigInteger)row.get("id"));
					dtlResponseDto.setPartNo((String)row.get("partNo"));
					dtlResponseDto.setPartDesc((String)row.get("partDesc"));
					dtlResponseDto.setProductCategory((String)row.get("productCategory"));
					dtlResponseDto.setProductSubCategory((String)row.get("productSubCategory"));
					dtlResponseDto.setStore((String)row.get("store"));
					dtlResponseDto.setStoreBinLocation((String)row.get("storeBinLocation"));
					dtlResponseDto.setSystemStock((BigDecimal)row.get("systemStock"));
					dtlResponseDto.setAdjustmentType((String)row.get("adjustmentType"));
					dtlResponseDto.setNetAdustmentQty((BigDecimal)row.get("netAdjustmentQty"));
					dtlResponseDto.setMrp((BigDecimal)row.get("mrp"));
					dtlResponseDto.setNdp((BigDecimal)row.get("ndp"));
					dtlResponseDto.setValue((BigDecimal)row.get("value"));
					dtlResponseDto.setReason((String)row.get("reason"));
					dtlResponseDto.setPartId((Integer) row.get("part_id"));
					dtlResponseDto.setBranchId((BigInteger) row.get("branch_id"));
					dtlResponseDto.setAdjustmentDtlId((BigInteger) row.get("sadtl_id"));
					dtlResponseDto.setStoreId((Integer)row.get("storeId"));
					
					dtlResponseDtos.add(dtlResponseDto);
				}
				responseDto.setPartDetails(dtlResponseDtos);
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
	    		apiResponse.setResult(responseDto);
	    		apiResponse.setMessage("Stock Adjustment View Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<?> approveRejectStockAdj(String userCode, StockAdjustmentApprovalRequestDto requestModel) {
	    Session session = null;
	    Transaction transaction = null;
	    boolean isSuccess = true;
	    
	    ApiResponse<StockAdjustmentApprovalResponseDto> apiResponse = null;
	    StockAdjustmentApprovalResponseDto responseModel = new StockAdjustmentApprovalResponseDto();
	    Map<String, Object> mapData = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();
	        
	        mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
	        BigInteger hoUserId = null;
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	        	hoUserId = (BigInteger) mapData.get("hoUserId");
	        }
	        
	        //Approving stock adjustment by the authority.
	        data = adjustmentDao.approveRejectStockAdj(session, userCode, hoUserId, requestModel);
	        
	        if (data != null && !data.isEmpty()) {
	            @SuppressWarnings("rawtypes")
				Map row = (Map) data.get(0);
	            String msg = (String) row.get("msg");
	            String approvalStatus = (String) row.get("approvalStatus");
	            
	            if(!approvalStatus.equalsIgnoreCase("FAIL") && msg.equalsIgnoreCase("SUCCESS")) {
	            	//Updating MRP of increasing adjustment items.
	            	List<?> dataList = adjustmentDao.updatePartMrp(session, requestModel.getAdjustmentId());
	            	
	            	//throwing error if MRP update failed.
	            	@SuppressWarnings("rawtypes")
					Map map = (Map)dataList.get(0);
	            	String message = (String)map.get("message");
	            	if(message.equalsIgnoreCase("failed")) {
	            		data = null;
	            		apiResponse = new ApiResponse<>();
	    	    		apiResponse.setMessage("Failed to update Part MRP!");
	    	    		apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
	            		throw new Exception("Failed to update Part MRP!");
	            	}
	            	
	            	//Updating bin part items quantity and amount
	            	for (Object object : dataList) {
						@SuppressWarnings("rawtypes")
						Map rowData = (Map) object;
						
						//Increasing bin items stocks quantity and amount.
						if(((String)rowData.get("adjType")).equalsIgnoreCase("Increase") && (BigInteger)rowData.get("partBranchId") != null) {
							String info = binTransferDao.updateStock(session, "ADD", (BigInteger)rowData.get("branchId"), 
									(BigInteger)rowData.get("partBranchId"), (BigInteger)rowData.get("stockStoreId"), 
									(BigInteger)rowData.get("binLocId"), (BigDecimal)rowData.get("adjQty"), null, 
									(BigDecimal)rowData.get("ndp"), "SP_STOCK_ADJUSTMENT_HDR", userCode);
							
							if(info.equalsIgnoreCase("failed")) {
								data = null;
			            		apiResponse = new ApiResponse<>();
			    	    		apiResponse.setMessage("Failed to update Bin Details!");
			    	    		apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
			            		throw new Exception("Failed to update Bin Details!");
							}
						}
						//decreasing bin items stocks quantity and amount.
						else if(((String)rowData.get("adjType")).equalsIgnoreCase("Decrease") && (BigInteger)rowData.get("partBranchId") != null) {
							String info = binTransferDao.updateStock(session, "SUBTRACT", (BigInteger)rowData.get("branchId"), 
									(BigInteger)rowData.get("partBranchId"), (BigInteger)rowData.get("stockStoreId"), 
									(BigInteger)rowData.get("binLocId"), null, (BigDecimal)rowData.get("adjQty"), 
									(BigDecimal)rowData.get("ndp"), "SP_STOCK_ADJUSTMENT_HDR", userCode);
							
							if(info.equalsIgnoreCase("failed")) {
								data = null;
			            		apiResponse = new ApiResponse<>();
			    	    		apiResponse.setMessage("Failed to update Bin Details!");
			    	    		apiResponse.setStatus(WebConstants.STATUS_EXPECTATION_FAILED_417);
			            		throw new Exception("Failed to update Bin Details!");
							}
						}
	            	}
	            	
	            	adjustmentDao.insertDataFromStoreTemptoUpload(session, requestModel.getAdjustmentId());
	            	
	            	adjustmentDao.uploadStockDataToMain(session);
	            	
	            	adjustmentDao.uploadStockAdjustmentDetail(session,requestModel.getAdPartDetails());
	            	
	            	
	            	responseModel.setMsg("Stock Adjustment Approved Successfully");	            	
	            }
	            else if (!approvalStatus.equalsIgnoreCase("FAIL") && msg.equalsIgnoreCase("REJECTED")) {
	            	responseModel.setMsg("Stock Adjustment Rejected Successfully");
	            }
	            else {
	            	responseModel.setMsg("Stock Adjustment Approval Failed");
	            }
	            
	            responseModel.setApprovalStatus(approvalStatus);
	            responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
	        } else {
	            responseModel.setMsg("Error While Validating Stock Adjustment Approval.");
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        }
	        
	        if (isSuccess) {
				transaction.commit();
			}

	    } catch (HibernateException ex) {
	    	if (transaction != null) transaction.rollback();
	        isSuccess = false;
	        data = null;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } catch (Exception ex) {
	        if (transaction != null) transaction.rollback();
	        isSuccess = false;
	        data = null;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } finally {
	    	if (data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(responseModel);
	    		apiResponse.setMessage("Operation Completed Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> getMrplist(BigInteger partId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = adjustmentDao.getMrplist(session, partId);

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
	    		apiResponse.setResult(data);
	    		apiResponse.setMessage("Get MRP Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoCompletePartNo(String partNo, BigInteger branchId) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = adjustmentDao.autoCompletePartNo(session, partNo, branchId);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("partBranchId", row.get("partBranchId"));
	                responseModel.put("partId", row.get("partId"));
	                responseModel.put("partNo", row.get("partNo"));
	                responseModel.put("partDesc", row.get("partDesc"));
	                responseModel.put("productCatgryId", row.get("productCategoryId"));
	                responseModel.put("productCatgry", row.get("productCatgry"));
	                responseModel.put("productSubCatgryId", row.get("productSubCategoryId"));
	                responseModel.put("productSubCatgry", row.get("productSubCatgry"));
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
	public ApiResponse<?> getStorelist(BigInteger partBranchId, BigInteger branchId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = adjustmentDao.getStorelist(session, partBranchId, branchId);

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
	    		apiResponse.setResult(data);
	    		apiResponse.setMessage("Get Store Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

}
