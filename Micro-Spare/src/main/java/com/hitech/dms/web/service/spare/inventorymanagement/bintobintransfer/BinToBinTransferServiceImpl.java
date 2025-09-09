package com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.spare.inventorymanagement.bintobintransfer.BinToBinTransferDao;
import com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer.BinToBinTransferDtl;
import com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer.BinToBinTransferHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.BinToBinTransferDetailDto;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.BinToBinTransferViewDto;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferResponseDto;

/**
 * @author suraj.gaur
 */
@Service
public class BinToBinTransferServiceImpl implements BinToBinTransferService {
	
	private static final Logger logger = LoggerFactory.getLogger(BinToBinTransferServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private BinToBinTransferDao binTransferDao;	
	
	@Override
	public ApiResponse<?> getSpareEmployee(BigInteger branchId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.getSpareEmployee(session, branchId);

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
	    		apiResponse.setMessage("Get Spare Employee Successfully.");
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
	        List<?> data = binTransferDao.autoCompletePartNo(session, partNo, branchId);

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

	        data = binTransferDao.getStorelist(session, partBranchId, branchId);

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
	
	@Override
	public ApiResponse<?> getStoreBinlist(String binName, BigInteger branchId, BigInteger stockStoreId, BigInteger partBranchId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.getStoreBinlist(session, binName, branchId, stockStoreId, partBranchId);

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
	    		apiResponse.setMessage("Get Store Bin Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<?> autoSearchIssueNo(String issueNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchIssueNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.autoSearchIssueNo(session, issueNo);

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
	    		apiResponse.setMessage("Auto Search Issue No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> autoSearchReceiptNo(String receiptNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchReceiptNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.autoSearchReceiptNo(session, receiptNo);

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
	    		apiResponse.setMessage("Auto Search Receipt No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<SearchBinToBinTransferResponseDto>> searchBinToBinTransfer(String userCode, SearchBinToBinTransferRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<SearchBinToBinTransferResponseDto>> apiResponse = null;
	    List<SearchBinToBinTransferResponseDto> responseList = null;
	    List<?> data = null;
	    Integer dataCount = 0;


	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.searchBinToBinTransfer(session, userCode, requestModel);

	        responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                SearchBinToBinTransferResponseDto response = new SearchBinToBinTransferResponseDto();
	                response.setId((BigInteger) row.get("issueId"));
	                response.setIssueNo((String) row.get("issueNo"));
	                response.setIssueDate((String) row.get("issueDate"));
	                response.setReceiptNo((String) row.get("receiptNo"));
	                response.setRemarks((String) row.get("remarks"));
	                response.setTransferDoneBy((String) row.get("transferDoneBy"));
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

//	        apiResponse.setCount(dataCount);
//	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

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
	    		apiResponse.setMessage("BIN TO BIN Transfer Search Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<?> viewBinToBinTransfer(BigInteger issueId) {
		Session session = null;
	    ApiResponse<BinToBinTransferViewDto> apiResponse = null;
	    BinToBinTransferViewDto responseDto = new BinToBinTransferViewDto();
	    List<BinToBinTransferDetailDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = binTransferDao.viewBinToBinTransfer(session, issueId, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setId((BigInteger)row.get("issueId"));
	        	responseDto.setIssueNo((String)row.get("issueNo"));
	        	responseDto.setIssueDate((Date)row.get("issueDate"));
				responseDto.setReceiptNo((String)row.get("receiptNo"));
				responseDto.setRemarks((String)row.get("remarks"));
				responseDto.setTransferDoneBy((String)row.get("transferDoneBy"));
	        }
	        
	        //for getting Detail Data
	        data = null;
	        data = binTransferDao.viewBinToBinTransfer(session, issueId, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					BinToBinTransferDetailDto dtlResponseDto = new BinToBinTransferDetailDto();
					dtlResponseDto.setIssueId((BigInteger)row.get("issueId"));
					dtlResponseDto.setPartNo((String)row.get("partNo"));
					dtlResponseDto.setPartDesc((String)row.get("partDesc"));
					dtlResponseDto.setFromStore((String)row.get("fromStore"));
					dtlResponseDto.setFromBinLocation((String)row.get("fromBinLocation"));
					dtlResponseDto.setFromBinStock((BigDecimal)row.get("fromBinStock"));
					dtlResponseDto.setToStore((String)row.get("toStore"));
					dtlResponseDto.setToBinLocation((String)row.get("toBinLocation"));
					dtlResponseDto.setToBinStock((BigDecimal)row.get("toBinStock"));
					dtlResponseDto.setTransferQty((BigDecimal)row.get("transferQty"));
					
					dtlResponseDtos.add(dtlResponseDto);
				}
				responseDto.setPartDetails(dtlResponseDtos);
	        }
	        
//	        apiResponse.setResult(responseDto);

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
	    		apiResponse.setMessage("Bin To Bin transfer View Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> saveBinToBinTransfer(String userCode, BinToBinTransferHdr requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveBinToBinTransfer invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<BinToBinTransferHdr> response = null;
		String bintoBinIssueNo = null;
		String binToBinReceiptNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				bintoBinIssueNo = commonDao.getDocumentNumberById("BTBI", branchId, session);
				commonDao.updateDocumentNumber("Bin To Bin Transfer Issue", branchId, bintoBinIssueNo, session);
				
				binToBinReceiptNo = commonDao.getDocumentNumberById("BTBR", branchId, session);
				commonDao.updateDocumentNumber("Bin To Bin Transfer Receipt", branchId, binToBinReceiptNo, session);
				
				requestModel.setIssueDate(new Date());
				requestModel.setIssueNo(bintoBinIssueNo);
				requestModel.setReceiptNo(binToBinReceiptNo);
				
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedDate(new Date());
		        }
				else {
					requestModel.setModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setModifiedDate(new Date());
		        }
				
				//New bin creation
				for(int i = 0; i < requestModel.getBinToBinTransferDtls().size(); i++) {
					BinToBinTransferDtl binToBinDtl = requestModel.getBinToBinTransferDtls().get(i);
					
					List<?> data = binTransferDao.createBin(session, branchId, binToBinDtl.getPartBranchId(),
							binToBinDtl.getStockStoreId(), binToBinDtl.getToBinName(), userCode);
					
					@SuppressWarnings("rawtypes")
					Map row = (Map) data.get(0);
					
					requestModel.getBinToBinTransferDtls().get(i).setToBinLocId((BigInteger)row.get("stockBinId"));
				}
				
				//Updating the stocks
				for(int i = 0; i < requestModel.getBinToBinTransferDtls().size(); i++) {
					BinToBinTransferDtl binToBinDtl = requestModel.getBinToBinTransferDtls().get(i);
					
					BigDecimal basicUnitPrice = binTransferDao.getBasicUnitPrice(session, userCode, branchId, binToBinDtl.getPartBranchId());
					
					//Adding Quantity in the bin stock
					String msg = binTransferDao.updateStock(session, "ADD", requestModel.getBranchId(), 
							binToBinDtl.getPartBranchId(), binToBinDtl.getToStoreMasterId(), 
							binToBinDtl.getToBinLocId(), binToBinDtl.getTransferQty(), null, basicUnitPrice, 
							"SP_BIN_TO_BIN_TRANSFER_HDR", userCode);
					
					//Subtracting Quantity from the bin stock if stock is successfully added
					if(msg.equals("Success")) {
						msg = binTransferDao.updateStock(session, "SUBTRACT", requestModel.getBranchId(), 
								binToBinDtl.getPartBranchId(), binToBinDtl.getFromStoreMasterId(), 
								binToBinDtl.getFromBinLocId(), null, binToBinDtl.getTransferQty(), basicUnitPrice, 
								"SP_BIN_TO_BIN_TRANSFER_HDR", userCode);
					}
					
					if(msg.equals("failed")) {
						response = new ApiResponse<>();
						response.setMessage("Stock Updation failed");
						response.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						
						throw new Exception("Stock Updation failed!");
					}
				}
				
				//Saving the data.
		        session.save(requestModel);
			} 
			
			//Committing the transaction if everything went well.
			if (isSuccess) {
				transaction.commit();
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (isSuccess) {
				if (requestModel != null) {
					response = new ApiResponse<>();
					response.setResult(requestModel);
					response.setMessage("Bin to Bin Transfer Issue Created Successfully.");
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
	public ApiResponse<?> getStockStores(BigInteger branchId, BigInteger partBranchId, BigInteger storeId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = binTransferDao.getStockStores(session, branchId, partBranchId, storeId);

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
	    		apiResponse.setMessage("Get Stock Store List Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

}
