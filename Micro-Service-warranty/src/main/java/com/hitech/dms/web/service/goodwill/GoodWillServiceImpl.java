package com.hitech.dms.web.service.goodwill;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.goodwill.GoodwillDao;
import com.hitech.dms.web.dao.pcr.pcrSearchDaoImpl;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwill;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillApproval;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillPhoto;
import com.hitech.dms.web.model.goodwill.GoodwillViewDto;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalResponseDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchResponseDto;
import com.hitech.dms.web.service.common.Utils;

/**
 * @author suraj.gaur
 * @apiNote this is the service Implementation
 */
@Service
public class GoodWillServiceImpl implements GoodWillService{
	
	private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private GoodwillDao goodwillDao; 

	@Override
	public ApiResponse<?> saveGoodwill(String authorizationHeader, String userCode, WarrantyGoodwill requestModel,
			List<MultipartFile> files) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("saveGoodwill invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<WarrantyGoodwill> response = new ApiResponse<>();
		String goodwillNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				goodwillNo = commonDao.getDocumentNumberById("WCG", branchId, session);
				commonDao.updateDocumentNumber("Warranty Claim Goodwill",branchId, goodwillNo, session);
			
				requestModel.setGoodwillNo(goodwillNo);
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedOn(new Date());
		        }
				else {
					requestModel.setLastModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setLastModifiedDate(new Date());
		        }
		        session.save(requestModel);
		        
		        BigInteger gwlId = requestModel.getId();
		        //Saving photo
		        if (files.size() <= 5 && !files.isEmpty()) {
		        	for(MultipartFile file: files){
		        		WarrantyGoodwillPhoto goodwillPhoto = new WarrantyGoodwillPhoto();
		                String originalFileName = file.getOriginalFilename();
		                String photoName = "PCR" + System.currentTimeMillis() + "_" + originalFileName;
		                utils.store(file, "wgc",gwlId, photoName);
		                goodwillPhoto.setFileType(file.getContentType());
		                goodwillPhoto.setFileName(photoName);
		                goodwillPhoto.setWarrantyGoodwill(requestModel);
		                
		                session.save(goodwillPhoto);
		            }
		        }
		        
		        
		        //Setting hierarchy in table
		        List<WarrantyGoodwillApproval> goodwillApprovals = new ArrayList<>();
				List<?> data = goodwillDao.getApprovalHierarchy(session);
				
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						@SuppressWarnings("rawtypes")
						Map row = (Map) object;
						
						WarrantyGoodwillApproval hierarchyData = new WarrantyGoodwillApproval();
						hierarchyData.setWarrantyGwlId(requestModel.getId());
						hierarchyData.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
						hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
						hierarchyData.setGrpSeqNo((Integer) row.get("grp_seq_no"));
						hierarchyData.setApprovalStatus((String) row.get("approvalStatus"));
						hierarchyData.setIsfinalapprovalstatus((Character) row.get("isFinalApprovalStatus"));
						hierarchyData.setRejectedFlag('N');

						goodwillApprovals.add(hierarchyData);
					}
				}		        
		        if (!goodwillApprovals.isEmpty()) {
		            for (WarrantyGoodwillApproval approvalEntity : goodwillApprovals) {
		                session.save(approvalEntity);
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
					response.setResult(requestModel);
					response.setMessage("Goodwill Number Created Successfully.");
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
	public ApiResponse<List<Map<String, Object>>> autoSearchGoodwillNo(String goodwillNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = goodwillDao.autoSearchGoodwillNo(session, goodwillNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("goodwillNo", row.get("goodwillNo"));
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
	public ApiResponse<List<Map<String, Object>>> autoSearchJcNo(String roNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = goodwillDao.autoSearchJcNo(session, roNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("Id", row.get("Id"));
	                responseModel.put("RONumber", row.get("RONumber"));
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
	public ApiResponse<List<Map<String, Object>>> autoSearchPcrNo(String pcrNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = goodwillDao.autoSearchPcrNo(session, pcrNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("pcrNo", row.get("pcrNo"));
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
	public ApiResponse<List<Map<String, Object>>> autoSearchChassisNo(String chassisNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = goodwillDao.autoSearchChassisNo(session, chassisNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("roId", row.get("ro_id"));
	                responseModel.put("serviceJobcardId", row.get("service_jobcard_id"));
	                responseModel.put("vinId", row.get("vin_id"));
	                responseModel.put("chassisNo", row.get("chassis_no"));
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
	public ApiResponse<List<GoodwillSearchResponseDto>> goodwillSearchList(String userCode, GoodwillSearchRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<GoodwillSearchResponseDto>> apiResponse = new ApiResponse<>();
	    Integer dataCount = 0;

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = goodwillDao.goodwillSearchList(session, userCode, requestModel);

	        List<GoodwillSearchResponseDto> responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                GoodwillSearchResponseDto response = new GoodwillSearchResponseDto();
	                response.setAction((String)row.get("Action"));
	                response.setId((BigInteger) row.get("goodwillId"));
	                response.setGoodwillNo((String) row.get("goodwill_no"));
	                response.setGoodwillDate((Date) row.get("goodwill_date"));
	                response.setDealerCode((String) row.get("DealerCode"));
	                response.setDealerName((String) row.get("DealerName"));
	                response.setBranch((String) row.get("BranchName"));
					response.setPcrId((BigInteger) row.get("pcrId"));
					response.setPcrNo((String) row.get("PCRNO"));
					response.setPcrCreatedDate((Date) row.get("PCRDATE"));
					response.setPcrSubmittedDate((Date) row.get("PCR_SUBMITED_DATE"));
					response.setStatus((String) row.get("status"));
					response.setJobCardNo((String) row.get("JobCard_No"));
					response.setJobCardDate((Date) row.get("JobCard_Date"));
					response.setChassisNo((String) row.get("chassis_no"));
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

	        apiResponse.setResult(responseList);
	        apiResponse.setCount(dataCount);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
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
	public ApiResponse<List<GoodwillApprovalResponseDto>> approveRejectGoodwill(String userCode, GoodwillApprovalRequestDto requestModel) {
	    Session session = null;
	    Transaction transaction = null;
	    boolean isSuccess = true;
	    
	    ApiResponse<List<GoodwillApprovalResponseDto>> apiResponse = new ApiResponse<>();
	    GoodwillApprovalResponseDto responseModel = new GoodwillApprovalResponseDto();

	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();
	        
	        List<?> data = goodwillDao.approveRejectGoodwill(session, userCode, requestModel);
	        
	        if (data != null && !data.isEmpty()) {
	            @SuppressWarnings("rawtypes")
				Map row = (Map) data.get(0);
	            String msg = (String) row.get("msg");
	            String approvalStatus = (String) row.get("approvalStatus");
	            
	            responseModel.setMsg(msg);
	            responseModel.setApprovalStatus(approvalStatus);

	            responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
	        } else {
	            responseModel.setMsg("Error While Validating Goodwill Approval.");
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        }
	        
	        if (isSuccess) {
				transaction.commit();
			}

	    } catch (HibernateException ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } catch (Exception ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } finally {
	        if (session != null) {
	            session.close();
	        }

	        apiResponse.setResult(Collections.singletonList(responseModel));
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> viewGoodwill(BigInteger goodwillId) 
	{		
		ApiResponse<GoodwillViewDto> apiResponse = new ApiResponse<>();
		
		Session session = null;
		GoodwillViewDto goodwillViewDto = new GoodwillViewDto();
		List<CustomerVoiceDto> customerVoiceDtoList;
	    List<ServiceHistoryDto> serviceHistoryDtoList;
	    List<LabourChargeDTO> outSideLabourChargeList;
	    List<LabourChargeDTO> labourChargeList;
	    List<JobCardPcrPartDto> JobCardPcrPartDtoList;		//failure part
	    List<WarrantyGoodwillPhoto> warrantyGoodwillPhotoList;
		
		List<?> data = null;
		
		try {
			session = sessionFactory.openSession();
			
			//For flag 1
			data = goodwillDao.getViewData(session, goodwillId, 1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					goodwillViewDto.setGoodwillNo((String)row.get("goodwillNo"));
					goodwillViewDto.setGoodwillDate((String)row.get("goodwillDate"));
					goodwillViewDto.setGoodwillStatus((String)row.get("goodwillStatus"));
					goodwillViewDto.setPcrNo((String)row.get("pcrNo"));
					goodwillViewDto.setPcrDate((Date)row.get("pcrDate"));
					goodwillViewDto.setPcrStatus((String)row.get("status"));
					goodwillViewDto.setWcrId((BigInteger)row.get("wcrId"));
					goodwillViewDto.setWcrNo((String)row.get("wcrNo"));
					goodwillViewDto.setWcrStatus((String)row.get("wcrStatus"));
					goodwillViewDto.setFinalStatus((String)row.get("finalStatus"));
					goodwillViewDto.setJobCardNo((String)row.get("jobCardNo"));
					goodwillViewDto.setJobCardDate((String)row.get("jobCardDate"));
					goodwillViewDto.setDateOfInstallation((String)row.get("dateOfInstallation"));
					goodwillViewDto.setCustomerName((String)row.get("CustomerName"));
					goodwillViewDto.setModel((String)row.get("model"));
					goodwillViewDto.setAddress((String)row.get("address"));
					goodwillViewDto.setMobileNo((String)row.get("mobileNo"));
					goodwillViewDto.setDateOfFailure((String)row.get("dateOfFailure"));
					goodwillViewDto.setTotalHour((BigInteger)row.get("Hours"));
					goodwillViewDto.setNatureOfFailure((String)row.get("natureOfFailure"));
					goodwillViewDto.setFailureType((String)row.get("failureType"));
					goodwillViewDto.setChassisNo((String)row.get("chassisNo"));
					goodwillViewDto.setEngineNo((String)row.get("engineNo"));
					goodwillViewDto.setVinNo((String)row.get("vinNo"));
					goodwillViewDto.setRegistrationNumber((String)row.get("registrationNumber"));
					goodwillViewDto.setSoldToDealer((String)row.get("soldToDealer"));
					goodwillViewDto.setServiceDealer((String)row.get("serviceDealer"));
					goodwillViewDto.setServiceDealerAddress((String)row.get("serviceDealerAddress"));
					goodwillViewDto.setComplaintCode((String)row.get("complaintCode"));
					goodwillViewDto.setComplaintAggregate((String)row.get("complaintAggregate"));
					goodwillViewDto.setServiceCategory((String)row.get("category"));
					goodwillViewDto.setServiceType((String)row.get("serviceType"));
					goodwillViewDto.setRepairOrderType((String)row.get("repairOrder"));
					goodwillViewDto.setRemark((String)row.get("remark"));
					goodwillViewDto.setRejectedReason((String)row.get("rejectedReason"));
					goodwillViewDto.setProductType((String)row.get("productType"));
				}
			}
			
			//For flag 2
			data = null;
			data = goodwillDao.getViewData(session, goodwillId, 2);
			
			if (data != null && !data.isEmpty()) {
				customerVoiceDtoList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					CustomerVoiceDto customerVoiceDto = new CustomerVoiceDto();
					customerVoiceDto.setCustomerConcern((String)row.get("customerConcern"));
					customerVoiceDto.setServiceRepresentative((String)row.get("serviceRepresentative"));
					customerVoiceDto.setActivityToDone((String)row.get("activityToDone"));
					
					customerVoiceDtoList.add(customerVoiceDto);
				}
				goodwillViewDto.setCustomerVoiceDto(customerVoiceDtoList);
			}
			
			//For flag 3
			data = null;
			data = goodwillDao.getViewData(session, goodwillId, 3);
			
			if (data != null && !data.isEmpty()) {
				outSideLabourChargeList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourChargeDTO outSideLabourCharge = new LabourChargeDTO();
					outSideLabourCharge.setLabourCode((String)row.get("LabourCode"));
					outSideLabourCharge.setLabourDescription((String)row.get("LabourDesc"));
					outSideLabourCharge.setHours((BigInteger)row.get("total_hour"));
					outSideLabourCharge.setClaimQty((int)row.get("ClaimQty"));
					outSideLabourCharge.setRate((BigDecimal)row.get("rate"));
					outSideLabourCharge.setTotalAmt((BigDecimal)row.get("totalAmt"));
					
					outSideLabourChargeList.add(outSideLabourCharge);
				}
				goodwillViewDto.setOutSideLabourCharge(outSideLabourChargeList);
			}
			
			//For flag 4
			data = null;
			data = goodwillDao.getViewData(session, goodwillId, 4);
			
			if (data != null && !data.isEmpty()) {
				labourChargeList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourChargeDTO labourCharge = new LabourChargeDTO();
					labourCharge.setLabourCode((String)row.get("LabourCode"));
					labourCharge.setLabourDescription((String)row.get("LabourDesc"));
					labourCharge.setHours((BigInteger)row.get("total_hour"));
					labourCharge.setClaimQty((int)row.get("ClaimQty"));
					labourCharge.setRate((BigDecimal)row.get("rate"));
					labourCharge.setTotalAmt((BigDecimal)row.get("totalAmt"));
					
					labourChargeList.add(labourCharge);
				}
				goodwillViewDto.setLabourCharge(labourChargeList);
			}
			
			//For flag 5
			data = null;			
			data = goodwillDao.getViewData(session, goodwillId, 5);
			
			if (data != null && !data.isEmpty()) {
				JobCardPcrPartDtoList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					JobCardPcrPartDto jobCardPcrPartDto = new JobCardPcrPartDto();
					jobCardPcrPartDto.setPartId((int)row.get("partId"));
					jobCardPcrPartDto.setPartNumber((String)row.get("partNumber"));
					jobCardPcrPartDto.setPartDesc((String)row.get("partDesc"));
					jobCardPcrPartDto.setClaimQty((BigDecimal)row.get("claimQty"));
					jobCardPcrPartDto.setApprovedQty((BigDecimal)row.get("approvedQty"));
					jobCardPcrPartDto.setFailureType((String)row.get("failureType"));
					jobCardPcrPartDto.setFailureCode((String)row.get("failureCode"));
					jobCardPcrPartDto.setFailureDescription((String)row.get("failureDesc"));
					
					JobCardPcrPartDtoList.add(jobCardPcrPartDto);
				}
				goodwillViewDto.setJobCardPcrPartDto(JobCardPcrPartDtoList);
			}
			
			//For flag 6
			data = null;
			data = goodwillDao.getViewData(session, goodwillId, 6);;
			
			if (data != null && !data.isEmpty()) {
				serviceHistoryDtoList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					ServiceHistoryDto serviceHistoryDto = new ServiceHistoryDto();
					serviceHistoryDto.setTypeOfService((String)row.get("TypeOfService"));
	                serviceHistoryDto.setHour((BigInteger)row.get("hour"));
	                serviceHistoryDto.setJobcardNo((String)row.get("JobcardNumber"));
	                serviceHistoryDto.setJobcardDate((Date)row.get("JobcardDate"));
	                serviceHistoryDto.setPcrNo(row.get("PcrNo").toString());
	                serviceHistoryDto.setPcrDate((Date)row.get("PcrDate"));

	                serviceHistoryDtoList.add(serviceHistoryDto);
				}
				goodwillViewDto.setServiceHistoryDto(serviceHistoryDtoList);
			}
			
			//For flag 7
			data = null;
			data = goodwillDao.getViewData(session, goodwillId, 7);
			
			if (data != null && !data.isEmpty()) {
				warrantyGoodwillPhotoList = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					WarrantyGoodwillPhoto warrantyGoodwillPhoto = new WarrantyGoodwillPhoto();
					warrantyGoodwillPhoto.setId((BigInteger)row.get("id"));
					warrantyGoodwillPhoto.setFileName((String)row.get("fileName"));
					warrantyGoodwillPhoto.setFileType((String)row.get("fileType"));
					
					warrantyGoodwillPhotoList.add(warrantyGoodwillPhoto);
				}
				goodwillViewDto.setWarrantyGoodwillPhoto(warrantyGoodwillPhotoList);
			}
			
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {

			if (goodwillViewDto != null) {
				apiResponse.setResult(goodwillViewDto);
				apiResponse.setMessage("Goodwill Number Created Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}

			if (session != null) {
				session.close();
			}
		}
		
		return apiResponse;
	}

}
