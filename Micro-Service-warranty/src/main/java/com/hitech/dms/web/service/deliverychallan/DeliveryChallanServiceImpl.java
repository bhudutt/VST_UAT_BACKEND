package com.hitech.dms.web.service.deliverychallan;

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

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.deliverychallan.DeliveryChallanDao;
import com.hitech.dms.web.dao.pcr.pcrSearchDaoImpl;
import com.hitech.dms.web.entity.deliverychallan.DeliveryChallanHdr;
import com.hitech.dms.web.model.deliverychallan.DcWcrItemListDto;
import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCRequestDto;
import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCResponceDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchGwlResponceDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchPcrResponceDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchRequestDto;

/**
 * @author suraj.gaur
 */

@Service
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

	private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;

	@Autowired
	private DeliveryChallanDao challanRepo;

	@Override
	public ApiResponse<?> wcrDispatchSearchList(String userCode, WcrDispatchRequestDto dispatchRequestDto)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("wcrDispatchSearchList invoked.." + userCode);
		}
		Session session = null;
		ApiResponse<List<?>> apiResponse = new ApiResponse<>();

		try {
			session = sessionFactory.openSession();

			List<?> data = null;
			if(dispatchRequestDto.getWcrType() != null) {
				data = challanRepo.wcrDispatchSearchList(session, userCode, dispatchRequestDto);
			}
			
			if (data != null && !data.isEmpty()) {
				if(dispatchRequestDto.getWcrType().equals("PCR")) {
					Integer dataCount = 0;
					List<WcrDispatchPcrResponceDto> responseList = new ArrayList<>();
					for (Object object : data) {
						WcrDispatchPcrResponceDto responceDto = new WcrDispatchPcrResponceDto();
						Map<?, ?> row = (Map<?, ?>) object;
						responceDto.setWcrId((BigInteger) row.get("wcrId"));
						responceDto.setWcrNo((String) row.get("wcrNo"));
						responceDto.setWcrDate((Date) row.get("wcrDate"));
						responceDto.setJobCardNo((String) row.get("JobCardNo"));
						responceDto.setJobCardDate((Date) row.get("jobCardDate"));
						responceDto.setPcrNo((String) row.get("pcrNo"));
						responceDto.setPcrDate((Date) row.get("pcrDate"));
						responceDto.setModel((String)row.get("modelName"));
						responceDto.setChassisNo((String) row.get("chassisNo"));
						responceDto.setVinNo((String) row.get("vinNo"));
						responceDto.setEngineNo((String) row.get("engineNo"));
						responceDto.setHour((BigInteger) row.get("hour"));
						responceDto.setWcrType((String) row.get("wcrType"));
						responceDto.setNoOfTime((BigInteger)row.get("noOfTime"));
						responceDto.setClaimValue((BigInteger)row.get("claimValue"));
						dataCount = (Integer) row.get("recordCount");

						responseList.add(responceDto);
					}
					
					apiResponse.setCount(dataCount);
					apiResponse.setResult(responseList);
					
				} else if (dispatchRequestDto.getWcrType().equals("Goodwill")) {
					Integer dataCount = 0;
					List<WcrDispatchGwlResponceDto> responseList = new ArrayList<>();
					for (Object object : data) {
						WcrDispatchGwlResponceDto responceDto = new WcrDispatchGwlResponceDto();
						Map<?, ?> row = (Map<?, ?>) object;
						responceDto.setWcrId((BigInteger) row.get("wcrId"));
						responceDto.setWcrNo((String) row.get("wcrNo"));
						responceDto.setWcrDate((Date) row.get("wcrDate"));
						responceDto.setJobCardNo((String) row.get("JobCardNo"));
						responceDto.setJobCardDate((Date) row.get("jobCardDate"));
						responceDto.setGoodwillNo((String) row.get("goodwillNo"));
						responceDto.setGoodwillDate((Date) row.get("goodwillDate"));
						responceDto.setModel((String)row.get("modelName"));
						responceDto.setChassisNo((String) row.get("chassisNo"));
						responceDto.setVinNo((String) row.get("vinNo"));
						responceDto.setEngineNo((String) row.get("engineNo"));
						responceDto.setHour((BigInteger) row.get("hour"));
						responceDto.setWcrType((String) row.get("wcrType"));
						responceDto.setNoOfTime((BigInteger)row.get("noOfTime"));
						responceDto.setClaimValue((BigInteger)row.get("claimValue"));
						dataCount = (Integer) row.get("recordCount");

						responseList.add(responceDto);
					}
					
					apiResponse.setCount(dataCount);
					apiResponse.setResult(responseList);
				}
				
			} else {
				apiResponse.setMessage("No data found!");
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
	public ApiResponse<?> saveDeliveryChallan(String authorizationHeader, String userCode,
			DeliveryChallanHdr requestModel) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("saveDeliveryChallan invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<DeliveryChallanHdr> response = new ApiResponse<>();
		String dcNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				dcNo = commonDao.getDocumentNumberById("WDC", branchId, session);
				commonDao.updateDocumentNumber("Warranty Delivery Challan", branchId, dcNo, session);
			
				requestModel.setDcNo(dcNo);
				requestModel.setStatus(requestModel.getDraftFlag() ? "Draft" : "Submitted");
				
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedDate(new Date());
		        }
				else {
					requestModel.setLastModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setLastModifiedDate(new Date());
		        }
				requestModel.setStatus(requestModel.getDraftFlag()? "Draft" : "Submitted");
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
					response.setResult(requestModel);
					response.setMessage("Delivery Challan Created Successfully.");
					response.setMessage(requestModel.getDraftFlag() 
							? "Saving Delivery Challan Successful" : "Submit Delivery Challan Successful");
					response.setStatus(WebConstants.STATUS_CREATED_201);
				}
			} else {
				response.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return response;
	}

	@Override
	public ApiResponse<?> wcrItemList(String userCode, String wcrIds) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("wcrItemList invoked.." + userCode);
		}
		Session session = null;
		ApiResponse<List<?>> apiResponse = new ApiResponse<>();

		try {
			session = sessionFactory.openSession();

			List<Map<String, Object>> data = null;
			data = challanRepo.wcrItemList(session, wcrIds);
			
			if (data != null && !data.isEmpty()) {
				List<DcWcrItemListDto> responseList = new ArrayList<>();
				for (Map<String, Object> map : data) {
					DcWcrItemListDto responceDto = new DcWcrItemListDto();
					Map<String, Object> row = map;
					responceDto.setWcrId((BigInteger) row.get("wcrId"));
					responceDto.setWcrNo((String) row.get("wcrNo"));
					responceDto.setPartNo((String)row.get("partNo"));
					responceDto.setPartDesc((String)row.get("partDesc"));
					responceDto.setHsnCode((String)row.get("hsnCode"));
					responceDto.setRate((BigDecimal)row.get("rate"));
					responceDto.setQty((BigDecimal)row.get("qty"));
					responceDto.setValue((BigDecimal)row.get("value"));
					responceDto.setGstPercentage((BigInteger)row.get("GSTPERCENTAGE"));
					responceDto.setGstAmount((BigDecimal)row.get("GST_AMOUNT"));

					responseList.add(responceDto);
				}
				apiResponse.setResult(responseList);
				apiResponse.setMessage("Data Getting Successfull");
				
			} else {
				apiResponse.setMessage("No data found!");
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
	public ApiResponse<?> fetchAllTranspoter(String userCode) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("fetchAllTranspoter invoked.." + userCode);
	    }
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = new ApiResponse<>();

	    try {
	        session = sessionFactory.openSession();

	        List<Map<String, Object>> data = null;
	        data = challanRepo.fetchAllTranspoter(session);
	        
	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseList = new ArrayList<>();
	            for (Map<String, Object> map : data) {
	                Map<String, Object> response = new HashMap<>();
	                Map<String, Object> row = map;
	                response.put("id", row.get("id"));
	                response.put("transporterName", row.get("transporterName")); // Corrected key here
	                responseList.add(response);
	            }
	            apiResponse.setResult(responseList);
	            apiResponse.setMessage("Data Getting Successfull");
	            
	        } else {
	            apiResponse.setMessage("No data found!");
	        }

	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> autoSearchDcNo(String dcNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchDcNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = challanRepo.autoSearchDcNo(session, dcNo);

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
	    		apiResponse.setMessage("Auto Search DC No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> autoSearchLrNo(String lrNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSearchLrNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = challanRepo.autoSearchLrNo(session, lrNo);

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
	    		apiResponse.setMessage("Auto Search LR No Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<?> warrantyPartsDCSearchList(String userCode, WarrantyPartsDCRequestDto requestModel)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("warrantyPartsDCSearchList invoked.." + userCode);
		}
		Session session = null;
		ApiResponse<List<?>> apiResponse = new ApiResponse<>();
		Integer dataCount = 0;
		List<WarrantyPartsDCResponceDto> responseList = new ArrayList<>();

		try {
			session = sessionFactory.openSession();

			List<?> data = null;
			data = challanRepo.warrantyPartsDCSearchList(session, userCode, requestModel);
			
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					WarrantyPartsDCResponceDto responceDto = new WarrantyPartsDCResponceDto();
					Map<?, ?> row = (Map<?, ?>) object;
					responceDto.setId((BigInteger)row.get("id"));
					responceDto.setDcNo((String)row.get("dcNo"));
					responceDto.setDcDate((Date)row.get("dcDate"));
					responceDto.setTranspoterName((String)row.get("transporterName"));
					responceDto.setLrNo((String)row.get("lrNo"));
					responceDto.setLrDate((Date)row.get("lrDate"));
					responceDto.setBaseAmount((BigDecimal)row.get("baseAmount"));
					responceDto.setGstAmount((BigDecimal)row.get("gstAmount"));
					responceDto.setTotalAmount((BigDecimal)row.get("totalAmount"));
					dataCount = (Integer) row.get("recordCount");

					responseList.add(responceDto);
				}
					
				apiResponse.setCount(dataCount);
				apiResponse.setResult(responseList);
					
			}else {
				apiResponse.setMessage("No data found!");
				apiResponse.setCount(dataCount);
				apiResponse.setResult(responseList);
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
	public ApiResponse<?> viewDcList(BigInteger id) {
		Session session = null;
	    ApiResponse<WarrantyPartsDCResponceDto> apiResponse = null;
	    WarrantyPartsDCResponceDto responseDto = new WarrantyPartsDCResponceDto();
	    List<DcWcrItemListDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = challanRepo.viewDcList(session, id, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setId((BigInteger)row.get("id"));
	        	responseDto.setDcNo((String)row.get("dcNo"));
	        	responseDto.setDcDate((Date)row.get("dcDate"));
	        	responseDto.setTranspoterName((String)row.get("transporterName"));
	        	responseDto.setLrNo((String)row.get("lrNo"));
	        	responseDto.setLrDate((Date)row.get("lrDate"));
				responseDto.setBaseAmount((BigDecimal)row.get("baseAmount"));
				responseDto.setGstAmount((BigDecimal)row.get("gstAmount"));
				responseDto.setTotalAmount((BigDecimal)row.get("totalAmount"));
	        }
	        
	        //for getting Detail Data
	        data = null;
	        data = challanRepo.viewDcList(session, id, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					DcWcrItemListDto dtlResponseDto = new DcWcrItemListDto();
					dtlResponseDto.setWcrId((BigInteger)row.get("wcrId"));
					dtlResponseDto.setWcrNo((String)row.get("wcrNo"));
					dtlResponseDto.setPartNo((String)row.get("partNo"));
					dtlResponseDto.setPartDesc((String)row.get("partDesc"));
					dtlResponseDto.setHsnCode((String)row.get("hsnCode"));
					dtlResponseDto.setRate((BigDecimal)row.get("rate"));
					dtlResponseDto.setQty((BigDecimal)row.get("qty"));
					dtlResponseDto.setValue((BigDecimal)row.get("value"));
					dtlResponseDto.setGstPercentage((BigInteger)row.get("gstPercentage"));
					dtlResponseDto.setGstAmount((BigDecimal)row.get("GST_AMOUNT"));
					
					dtlResponseDtos.add(dtlResponseDto);
				}
				responseDto.setDcDetailList(dtlResponseDtos);
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
	    		apiResponse.setMessage("Warranty Parts DC View Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

}
