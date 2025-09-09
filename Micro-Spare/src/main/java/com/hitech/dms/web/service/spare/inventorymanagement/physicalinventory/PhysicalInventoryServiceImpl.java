package com.hitech.dms.web.service.spare.inventorymanagement.physicalinventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
import com.hitech.dms.web.dao.spare.inventorymanagement.physicalinventory.PhysicalInventoryDao;
import com.hitech.dms.web.dao.spare.inventorymanagement.stockadjustment.StockAdjustmentDao;
import com.hitech.dms.web.entity.partmaster.create.request.BranchPartMasterEntity;
import com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory.PhysicalInventoryDtl;
//import com.hitech.dms.web.entity.partmaster.create.request.BranchPartMasterEntity;
import com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory.PhysicalInventoryHdr;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentApproval;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentDtl;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.PhysicalInventoryDetailDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.PhysicalInvntoryViewDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryResponseDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.StockAdjForPhyInvDto;
import com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer.BinToBinTransferServiceImpl;
import com.hitech.dms.web.service.spare.inventorymanagement.stockadjustment.StockAdjustmentServiceImpl;

@Service
public class PhysicalInventoryServiceImpl implements PhysicalInventoryService{
	
	private static final Logger logger = LoggerFactory.getLogger(BinToBinTransferServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private StockAdjustmentDao adjustmentDao;
	
	@Autowired
	private PhysicalInventoryDao PhysicalInvDao;
	
	@Autowired
	private StockAdjustmentServiceImpl stockAdjustmentServiceImpl;
	
	@Override
	public ApiResponse<?> getProductCatgry() {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.getProductCatgry(session);

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
	    		apiResponse.setMessage("Get Product Category Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> getPartsDetail(BigInteger branchId, BigInteger prodCatId, Boolean isZeroQty) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.getPartsDetail(session, branchId, prodCatId, isZeroQty);

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
	    		apiResponse.setMessage("Get Part Details List Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<?> getToStores(BigInteger branchId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.getToStores(session, branchId);

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
	    		apiResponse.setMessage("Get Store Details List Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> getToBinLocation(BigInteger partBranchId, BigInteger storeId, String binLocation) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.getToBinLocation(session, partBranchId, storeId, binLocation);

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
	    		apiResponse.setMessage("Get BinLocation Details List Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> getAllNdp(BigInteger partBranchId, BigInteger storeId, BigInteger binId) {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.getAllNdp(session, partBranchId, storeId, binId);

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
	    		apiResponse.setMessage("Get NDP List Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<?> savePhysicalInventory(String userCode, StockAdjForPhyInvDto requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("savePhysicalInventory invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<StockAdjForPhyInvDto> response = null;
		String phyInvNo = null;
		String adjustmentNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getPhysicalInventoryHdr().getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				
				if(requestModel.getPhysicalInventoryHdr().getId() == null) {
					phyInvNo = commonDao.getDocumentNumberById("PI", branchId, session);
					commonDao.updateDocumentNumber("Physical Inventory", branchId, phyInvNo, session);
					
					requestModel.getPhysicalInventoryHdr().setPhysicalInvNo(phyInvNo);
					requestModel.getPhysicalInventoryHdr().setPhysicalInvDate(new Date());
					requestModel.getPhysicalInventoryHdr().setStatus("Generated");
					
					requestModel.getPhysicalInventoryHdr().setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.getPhysicalInventoryHdr().setCreatedDate(new Date());

					session.save(requestModel.getPhysicalInventoryHdr());
					
					//locking the transaction for particular parts. added code on 01-12-2023
					for(PhysicalInventoryDtl detail : requestModel.getPhysicalInventoryHdr().getPhysicalInvDtl()) {
						BigInteger partBranchId = detail.getPartBranchId();
					
						BranchPartMasterEntity partMaster = session
						.createQuery("FROM BranchPartMasterEntity WHERE branch_id = :branchId AND partBranch_id = :partBranchId", BranchPartMasterEntity.class)
			            .setParameter("branchId", branchId)
			            .setParameter("partBranchId", partBranchId) // Make sure you have partId available
			            .uniqueResult();
						if(partMaster != null) {
							partMaster.setLockForTranscationStatus(true);
						
							session.save(partMaster);
						}
					}
					
		        }
				
				else{
						requestModel.getPhysicalInventoryHdr().setStatus("Submitted");
						requestModel.getPhysicalInventoryHdr().setModifiedBy((BigInteger)mapData.get("usetId"));
						requestModel.getPhysicalInventoryHdr().setModifiedDate(new Date());
						
						session.saveOrUpdate(requestModel.getPhysicalInventoryHdr());
						
						//generating Stock Adjustment No. added on 01-12-2023
						
//						response = new ApiResponse<>();
						
						if(requestModel.getStockAdjustment().getId() == null) {
							ApiResponse<?> savedStockAdjustmentResponse = stockAdjustmentServiceImpl.saveStockAdjustment(userCode, requestModel.getStockAdjustment());
							
							if (savedStockAdjustmentResponse.getResult() instanceof SpareStockAdjustmentHdr) {
						        SpareStockAdjustmentHdr savedStockAdjustment = (SpareStockAdjustmentHdr) savedStockAdjustmentResponse.getResult();

						        // Now, you can get the Stock Adjustment ID
						        BigInteger stockAdjustmentId = savedStockAdjustment.getId();

						        // Save the Stock Adjustment ID into PhysicalInventoryHdr
						        requestModel.getPhysicalInventoryHdr().setAdjustmentId(stockAdjustmentId);
						    }
						}
		        }
				
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
					response.setMessage("Physical Inventory Created Successfully.");
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
	public ApiResponse<?> autoSearchPhyInvNo(String phyInvNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchPhyInvNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.autoSearchPhyInvNo(session, phyInvNo);

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
	    		apiResponse.setMessage("Auto Search Physical Inventory No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<SearchPhysicalInventoryResponseDto>> searchPhysicalInventory(String userCode, SearchPhysicalInventoryRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<SearchPhysicalInventoryResponseDto>> apiResponse = null;
	    List<SearchPhysicalInventoryResponseDto> responseList = null;
	    List<?> data = null;
	    Integer dataCount = 0;


	    try {
	        session = sessionFactory.openSession();

	        data = PhysicalInvDao.searchPhysicalInventory(session, userCode, requestModel);

	        responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                SearchPhysicalInventoryResponseDto response = new SearchPhysicalInventoryResponseDto();
	                response.setId((BigInteger)row.get("id"));
	                response.setPhyInvNo((String)row.get("phyInvNo"));
	                response.setPhyInvDate((String)row.get("phyInvDate"));
	                response.setProductCategory((String)row.get("productCategory"));
	                response.setStatus((String) row.get("status"));
	                response.setRemarks((String) row.get("remarks"));
	                response.setPhyInvDoneBy((String) row.get("phyInvDoneBy"));
	                response.setAdjustmentNo((String) row.get("adjustmentNo"));
	                response.setAdjustmentStatus((String) row.get("adjustmentStatus"));
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
	    		apiResponse.setMessage("Physical Inventory Search Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> viewPhysicalInventory(BigInteger phyInvId) {
		Session session = null;
	    ApiResponse<PhysicalInvntoryViewDto> apiResponse = null;
	    PhysicalInvntoryViewDto responseDto = new PhysicalInvntoryViewDto();
	    List<PhysicalInventoryDetailDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = PhysicalInvDao.viewPhysicalInventory(session, phyInvId, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setId((BigInteger)row.get("id"));
	        	responseDto.setBranchId((BigInteger)row.get("branchId"));
	        	responseDto.setBranchName((String)row.get("branchName"));
	        	responseDto.setPhyInvNo((String)row.get("phyInvNo"));
	        	responseDto.setPhyInvDate((Date)row.get("phyInvDate"));
				responseDto.setStatus((String)row.get("status"));
				responseDto.setProductCatId((BigInteger)row.get("productCatId"));
				responseDto.setProductCat((String)row.get("productCat"));
				responseDto.setPhyInvDoneById((BigInteger)row.get("phyInvDoneById"));
				responseDto.setPhyInvDoneBy((String)row.get("phyInvDoneBy"));
				responseDto.setRemarks((String)row.get("remarks"));
				responseDto.setAdjustmentId((BigInteger)row.get("adjustmentId"));
	        	responseDto.setAdjustmentNo((String)row.get("adjustmentNo"));
	        	responseDto.setAdjStatus((String)row.get("adjustmentStatus"));
	        	responseDto.setIsZeroQty((BigDecimal)row.get("isZeroQty"));
				responseDto.setTotatIncreseQty((BigDecimal)row.get("totalIncQty"));
				responseDto.setTotatIncreseValue((BigDecimal)row.get("totalIncVal"));
				responseDto.setTotatDecreaseQty((BigDecimal)row.get("totalDecQty"));
				responseDto.setTotatDecreaseValue((BigDecimal)row.get("totalDecVal"));
	        }
	        
	        //for getting Detail Data
	        data = null;
	        data = PhysicalInvDao.viewPhysicalInventory(session, phyInvId, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					PhysicalInventoryDetailDto dtlResponseDto = new PhysicalInventoryDetailDto();
					dtlResponseDto.setId((BigInteger)row.get("id"));
					dtlResponseDto.setPhyInvId((BigInteger)row.get("phyInvId"));
					dtlResponseDto.setPartBranchId((BigInteger)row.get("partBranchId"));
					dtlResponseDto.setPartNo((String)row.get("partNo"));
					dtlResponseDto.setPartDesc((String)row.get("partDesc"));
					dtlResponseDto.setProductCategoryId((BigInteger)row.get("productCatId"));
					dtlResponseDto.setProductCategory((String)row.get("productCat"));
					dtlResponseDto.setProductSubCategoryId((BigInteger)row.get("productSubCatId"));
					dtlResponseDto.setProductSubCategory((String)row.get("productSubCat"));
					dtlResponseDto.setFromStoreId((BigInteger)row.get("fromStoreId"));
					dtlResponseDto.setFromStore((String)row.get("fromStore"));
					dtlResponseDto.setFromBinId((BigInteger)row.get("fromBinId"));
					dtlResponseDto.setFromStoreBinLocation((String)row.get("fromBin"));
					dtlResponseDto.setSystemStock((BigDecimal)row.get("systemStock"));
					dtlResponseDto.setPhysicalInventory((BigDecimal)row.get("phyInv"));
					dtlResponseDto.setToStoreId((BigInteger)row.get("toStoreId"));
					dtlResponseDto.setToStore((String)row.get("toStore"));
					dtlResponseDto.setToBinId((BigInteger)row.get("toBinId"));
					dtlResponseDto.setToStoreBinLocation((String)row.get("toBin"));
					dtlResponseDto.setNetAdustmentQty((BigDecimal)row.get("netAdjustmentQty"));
					dtlResponseDto.setNdp((BigDecimal)row.get("ndp"));
//					dtlResponseDto.setDiffValue((BigDecimal)row.get("value"));
					dtlResponseDto.setReason((String)row.get("reason"));
					dtlResponseDto.setPartLockForTrans((String)row.get("partLockForTrans"));
					
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
	    		apiResponse.setMessage("Physical Inventory View Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

}
